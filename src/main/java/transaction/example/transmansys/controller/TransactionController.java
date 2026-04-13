package transaction.example.transmansys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import transaction.example.transmansys.dto.TransactionResponseDTO;
import transaction.example.transmansys.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<TransactionResponseDTO> getAll() {
        return transactionService.getAll(); // ✅ correct
    }

    @GetMapping("/{id}")
    public TransactionResponseDTO getById(@PathVariable Long id) {
        return transactionService.getById(id); // ✅ correct
    }
}