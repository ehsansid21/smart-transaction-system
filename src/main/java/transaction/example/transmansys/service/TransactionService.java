package transaction.example.transmansys.service;

import transaction.example.transmansys.dto.TransactionResponseDTO;
import transaction.example.transmansys.exception.BadRequestException;
import transaction.example.transmansys.entity.Transaction;
import transaction.example.transmansys.entity.User;
import transaction.example.transmansys.repository.TransactionRepository;
import transaction.example.transmansys.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import transaction.example.transmansys.dto.PaginatedResponseDTO;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ExchangeRateService exchangeRateService;

    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository,
                              EmailService emailService,
                              ExchangeRateService exchangeRateService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.exchangeRateService = exchangeRateService;
    }

    // 💸 TRANSFER BY EMAIL (called from controller with JWT-derived sender email)
    @Transactional
    public TransactionResponseDTO transferByEmail(String senderEmail, String receiverEmail, Double amount, String currency) {

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        Double amountInINR = exchangeRateService.convertToINR(amount, currency);

        if (senderEmail.equalsIgnoreCase(receiverEmail)) {
            throw new BadRequestException("You cannot transfer money to yourself");
        }

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new RuntimeException("No user found with email: " + receiverEmail));

        BigDecimal transferAmount = BigDecimal.valueOf(amountInINR);

        if (sender.getBalance() == null) sender.setBalance(BigDecimal.ZERO);
        if (receiver.getBalance() == null) receiver.setBalance(BigDecimal.ZERO);

        if (sender.getBalance().compareTo(transferAmount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 💰 update balances
        sender.setBalance(sender.getBalance().subtract(transferAmount));
        receiver.setBalance(receiver.getBalance().add(transferAmount));

        userRepository.save(sender);
        userRepository.save(receiver);

        // 🧾 save transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(transferAmount);
        transaction.setSenderId(sender.getId());
        transaction.setReceiverId(receiver.getId());
        transaction.setTimestamp();

        Transaction saved = transactionRepository.save(transaction);
        
        // 📧 trigger email receipt (mock)
        emailService.sendTransferReceipt(sender.getEmail(), sender.getName(), receiver.getName(), transferAmount);

        return mapToDTO(saved, sender, receiver, "DEBIT");
    }

    // 💰 ADD FUNDS (Deposit from System/Bank)
    @Transactional
    public TransactionResponseDTO depositFunds(String email, Double amount) {
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal depositAmount = BigDecimal.valueOf(amount);

        if (user.getBalance() == null) user.setBalance(BigDecimal.ZERO);
        user.setBalance(user.getBalance().add(depositAmount));
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setAmount(depositAmount);
        transaction.setSenderId(null); // System deposit
        transaction.setReceiverId(user.getId());
        transaction.setTimestamp();

        Transaction saved = transactionRepository.save(transaction);
        return mapToDTO(saved, null, user, "CREDIT");
    }

    // 💸 LEGACY: TRANSFER BY ID (kept for compatibility)
    @Transactional
    public Transaction transferMoney(Long senderId, Long receiverId, Double amount) {
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }
        if (senderId.equals(receiverId)) {
            throw new BadRequestException("Sender and receiver cannot be same");
        }
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        BigDecimal transferAmount = BigDecimal.valueOf(amount);
        if (sender.getBalance() == null) sender.setBalance(BigDecimal.ZERO);
        if (receiver.getBalance() == null) receiver.setBalance(BigDecimal.ZERO);
        if (sender.getBalance().compareTo(transferAmount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        sender.setBalance(sender.getBalance().subtract(transferAmount));
        receiver.setBalance(receiver.getBalance().add(transferAmount));
        userRepository.save(sender);
        userRepository.save(receiver);
        Transaction transaction = new Transaction();
        transaction.setAmount(transferAmount);
        transaction.setSenderId(senderId);
        transaction.setReceiverId(receiverId);
        transaction.setTimestamp();
        return transactionRepository.save(transaction);
    }

    // 📜 GET ALL TRANSACTIONS (Admin)
    public PaginatedResponseDTO<TransactionResponseDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Transaction> paged = transactionRepository.findAll(pageable);
        List<TransactionResponseDTO> dtos = paged.getContent().stream().map(t -> {
            User s = t.getSenderId() != null ? userRepository.findById(t.getSenderId()).orElse(null) : null;
            User r = t.getReceiverId() != null ? userRepository.findById(t.getReceiverId()).orElse(null) : null;
            return mapToDTO(t, s, r, "CREDIT");
        }).collect(Collectors.toList());
        return new PaginatedResponseDTO<>(dtos, paged.getNumber(), paged.getSize(), paged.getTotalElements(), paged.getTotalPages(), paged.isLast());
    }

    // 👤 USER HISTORY BY USER ID (for legacy admin use)
    public List<Transaction> getUserTransactions(Long userId) {
        List<Transaction> sent = transactionRepository.findBySenderId(userId);
        List<Transaction> received = transactionRepository.findByReceiverId(userId);
        List<Transaction> all = new ArrayList<>();
        all.addAll(sent);
        all.addAll(received);
        return all;
    }

    // 👤 USER HISTORY BY EMAIL (Paginated & Filtered)
    public PaginatedResponseDTO<TransactionResponseDTO> getUserTransactionsByEmail(String email, int page, int size, LocalDateTime startDate, LocalDateTime endDate, String search) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Transaction> paged = transactionRepository.findFilteredTransactions(userId, startDate, endDate, search, pageable);

        List<TransactionResponseDTO> dtos = paged.getContent().stream().map(t -> {
            User s = t.getSenderId() != null ? userRepository.findById(t.getSenderId()).orElse(null) : null;
            User r = t.getReceiverId() != null ? userRepository.findById(t.getReceiverId()).orElse(null) : null;
            String type = t.getSenderId() != null && t.getSenderId().equals(userId) ? "DEBIT" : "CREDIT";
            return mapToDTO(t, s, r, type);
        }).collect(Collectors.toList());

        return new PaginatedResponseDTO<>(dtos, paged.getNumber(), paged.getSize(), paged.getTotalElements(), paged.getTotalPages(), paged.isLast());
    }
    
    // 📊 UNPAGINATED FOR DASHBOARD (returns all to calculate chart stats for now)
    public List<TransactionResponseDTO> getAllUserTransactionsByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();

        List<Transaction> sent = transactionRepository.findBySenderId(userId);
        List<Transaction> received = transactionRepository.findByReceiverId(userId);

        List<TransactionResponseDTO> result = new ArrayList<>();

        for (Transaction t : sent) {
            User receiver = userRepository.findById(t.getReceiverId()).orElse(null);
            result.add(mapToDTO(t, user, receiver, "DEBIT"));
        }

        for (Transaction t : received) {
            User sender = userRepository.findById(t.getSenderId()).orElse(null);
            result.add(mapToDTO(t, sender, user, "CREDIT"));
        }

        result.sort(Comparator.comparing(TransactionResponseDTO::getTimestamp,
                Comparator.nullsLast(Comparator.reverseOrder())));

        return result;
    }

    // 🔧 MAP to DTO
    private TransactionResponseDTO mapToDTO(Transaction t, User sender, User receiver, String type) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(t.getId());
        dto.setAmount(t.getAmount());
        dto.setSenderId(t.getSenderId());
        dto.setReceiverId(t.getReceiverId());
        dto.setSenderName(sender != null ? sender.getName() : "System (Bank Deposit)");
        dto.setReceiverName(receiver != null ? receiver.getName() : "Unknown");
        dto.setSenderEmail(sender != null ? sender.getEmail() : "system@bank.com");
        dto.setReceiverEmail(receiver != null ? receiver.getEmail() : "");
        dto.setTimestamp(t.getTimestamp() != null ? t.getTimestamp().toString() : null);
        dto.setType(type);
        return dto;
    }
}