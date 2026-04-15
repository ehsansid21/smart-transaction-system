package transaction.example.transmansys.repository;

import transaction.example.transmansys.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ✅ FETCH USER HISTORY
    List<Transaction> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}