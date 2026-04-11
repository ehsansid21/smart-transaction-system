package transaction.example.transmansys.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import transaction.example.transmansys.entity.User;
import transaction.example.transmansys.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // CREATE
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // READ ALL
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ BY ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // UPDATE
    public User updateUser(Long id, User updatedUser) {
        User user = getUserById(id);

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());

        return userRepository.save(user);
    }

    // DELETE
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 🔥 MONEY TRANSFER
    @Transactional
    public void transferMoney(Long senderId, Long receiverId, BigDecimal amount) {

        User sender = getUserById(senderId);
        User receiver = getUserById(receiverId);

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        userRepository.save(sender);
        userRepository.save(receiver);
    }
}