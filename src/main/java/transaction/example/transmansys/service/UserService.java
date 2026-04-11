package transaction.example.transmansys.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import transaction.example.transmansys.entity.User;
import transaction.example.transmansys.exception.InsufficientBalanceException;
import transaction.example.transmansys.exception.UserAlreadyExistsException;
import transaction.example.transmansys.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {

        // Check duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered!");
        }

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User updateUser(Long id, User updatedUser) {

        User user = getUserById(id);

        // Check email uniqueness (only if changed)
        if (!user.getEmail().equals(updatedUser.getEmail()) &&
                userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already in use!");
        }

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public void transferMoney(Long senderId, Long receiverId, BigDecimal amount) {

        // Validation
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Sender and receiver cannot be same");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        User sender = getUserById(senderId);
        User receiver = getUserById(receiverId);

        // Balance check
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Transfer logic
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        userRepository.save(sender);
        userRepository.save(receiver);
    }
}