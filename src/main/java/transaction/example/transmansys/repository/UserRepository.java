package transaction.example.transmansys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transaction.example.transmansys.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}