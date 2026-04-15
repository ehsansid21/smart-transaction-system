package transaction.example.transmansys.service;

import transaction.example.transmansys.entity.Transaction;
import transaction.example.transmansys.entity.User;
import transaction.example.transmansys.repository.TransactionRepository;
import transaction.example.transmansys.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    // 🔥 VERY IMPORTANT
    @Transactional
    public Transaction transferMoney(Long senderId, Long receiverId, Double amount) {

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        if (senderId.equals(receiverId)) {
            throw new RuntimeException("Sender and receiver cannot be same");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        BigDecimal transferAmount = BigDecimal.valueOf(amount);

        if (sender.getBalance() == null) {
            sender.setBalance(BigDecimal.ZERO);
        }

        if (receiver.getBalance() == null) {
            receiver.setBalance(BigDecimal.ZERO);
        }

        if (sender.getBalance().compareTo(transferAmount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 💸 TRANSFER
        sender.setBalance(sender.getBalance().subtract(transferAmount));
        receiver.setBalance(receiver.getBalance().add(transferAmount));

        userRepository.save(sender);
        userRepository.save(receiver);

        // 🧾 SAVE TRANSACTION
        Transaction transaction = new Transaction();
        transaction.setAmount(transferAmount);
        transaction.setSenderId(senderId);
        transaction.setReceiverId(receiverId);

        // ❗ REMOVE createdAt (handled by @PrePersist if you added it)
        return transactionRepository.save(transaction);
    }

    // ✅ GET ALL
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    // ✅ USER HISTORY (CLEAN VERSION)
    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findBySenderIdOrReceiverId(userId, userId);
    }
}