package transaction.example.transmansys.controller;

import transaction.example.transmansys.entity.Transaction;
import transaction.example.transmansys.service.TransactionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // ✅ TRANSFER MONEY
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam BigDecimal amount
    ) {
        Transaction tx = transactionService.transferMoney(senderId, receiverId, amount.doubleValue());
        return ResponseEntity.ok(tx);
    }

    // ✅ GET ALL TRANSACTIONS
    @GetMapping
    public ResponseEntity<List<Transaction>> getAll() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    // ✅ GET USER TRANSACTION HISTORY
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getUserTransactions(userId));
    }
}