package transaction.example.transmansys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transaction.example.transmansys.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}