package transaction.example.transmansys.service;

import transaction.example.transmansys.dto.TransactionResponseDTO;
import transaction.example.transmansys.entity.Transaction;
import transaction.example.transmansys.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // ✅ GET ALL TRANSACTIONS
    public List<TransactionResponseDTO> getAll() {
        return transactionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ✅ GET TRANSACTION BY ID
    public TransactionResponseDTO getById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        return mapToDTO(transaction);
    }

    // 🔁 ENTITY → DTO MAPPER
    private TransactionResponseDTO mapToDTO(Transaction t) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(t.getId());
        dto.setAmount(t.getAmount());
        dto.setSenderId(t.getSender().getId());
        dto.setReceiverId(t.getReceiver().getId());
        return dto;
    }
}