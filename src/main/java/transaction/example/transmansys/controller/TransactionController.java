package transaction.example.transmansys.controller;

import transaction.example.transmansys.entity.Transaction;
import transaction.example.transmansys.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public Transaction transfer(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam Double amount
    ) {
        return transactionService.transferMoney(senderId, receiverId, amount);
    }

    @GetMapping
    public List<Transaction> getAll() {
        return transactionService.getAll();
    }

    @GetMapping("/user/{userId}")
    public List<Transaction> getUserTransactions(@PathVariable Long userId) {
        return transactionService.getUserTransactions(userId);
    }
}