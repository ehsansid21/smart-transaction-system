package transaction.example.transmansys.controller;

import transaction.example.transmansys.dto.TransactionResponseDTO;
import transaction.example.transmansys.entity.Transaction;
import transaction.example.transmansys.service.ExchangeRateService;
import transaction.example.transmansys.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import transaction.example.transmansys.dto.PaginatedResponseDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final ExchangeRateService exchangeRateService;

    public TransactionController(TransactionService transactionService, ExchangeRateService exchangeRateService) {
        this.transactionService = transactionService;
        this.exchangeRateService = exchangeRateService;
    }

    // ✅ TRANSFER — sender identified from JWT, receiver by email, amount from body
    @PostMapping("/transfer")
    public TransactionResponseDTO transfer(
            @RequestBody Map<String, Object> body,
            Authentication auth) {

        String senderEmail = auth.getName();
        String receiverEmail = (String) body.get("receiverEmail");
        double amount = Double.parseDouble(body.get("amount").toString());
        String currency = body.containsKey("currency") ? (String) body.get("currency") : "INR";

        return transactionService.transferByEmail(senderEmail, receiverEmail, amount, currency);
    }

    // 💱 GET LIVE EXCHANGE RATES
    @GetMapping("/rates")
    public Map<String, Object> getRates() {
        return exchangeRateService.getRates();
    }

    // ✅ DEPOSIT — adds funds to logged-in user
    @PostMapping("/deposit")
    public TransactionResponseDTO deposit(
            @RequestBody Map<String, Object> body,
            Authentication auth) {
        
        String email = auth.getName();
        double amount = Double.parseDouble(body.get("amount").toString());
        return transactionService.depositFunds(email, amount);
    }

    // ✅ HISTORY for currently logged-in user (Paginated & Filtered)
    @GetMapping("/history")
    public PaginatedResponseDTO<TransactionResponseDTO> getMyHistory(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String search) {
            
        LocalDateTime start = (startDate != null && !startDate.isEmpty()) ? LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME) : null;
        LocalDateTime end = (endDate != null && !endDate.isEmpty()) ? LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME) : null;
        
        return transactionService.getUserTransactionsByEmail(auth.getName(), page, size, start, end, search);
    }
    
    // ✅ DASHBOARD (Unpaginated)
    @GetMapping("/dashboard")
    public List<TransactionResponseDTO> getDashboardHistory(Authentication auth) {
        return transactionService.getAllUserTransactionsByEmail(auth.getName());
    }

    // ✅ ALL TRANSACTIONS (admin, paginated)
    @GetMapping
    public PaginatedResponseDTO<TransactionResponseDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return transactionService.getAll(page, size);
    }

    // ✅ TRANSACTIONS BY USER ID (kept for admin/legacy use)
    @GetMapping("/user/{userId}")
    public List<Transaction> getUserTransactions(@PathVariable Long userId) {
        return transactionService.getUserTransactions(userId);
    }
}