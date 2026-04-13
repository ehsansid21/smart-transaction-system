package transaction.example.transmansys.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import transaction.example.transmansys.dto.UserRequestDTO;
import transaction.example.transmansys.dto.UserResponseDTO;
import transaction.example.transmansys.entity.User;
import transaction.example.transmansys.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    // ✅ CREATE
    public UserResponseDTO createUser(UserRequestDTO dto) {

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setBalance(dto.getBalance());

        User saved = userRepository.save(user);

        return mapToResponse(saved);
    }

    // ✅ GET ALL
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ GET BY ID (ENTITY)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ GET BY ID (DTO) ⭐ FIX
    public UserResponseDTO getUserResponseById(Long id) {
        User user = getUserById(id);
        return mapToResponse(user);
    }

    // ✅ UPDATE USER ⭐ FIX
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        User user = getUserById(id);

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setBalance(dto.getBalance());

        User updated = userRepository.save(user);

        return mapToResponse(updated);
    }

    // ✅ DELETE
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ✅ TRANSFER
    @Transactional
    public void transferMoney(Long senderId, Long receiverId, BigDecimal amount) {

        User sender = getUserById(senderId);
        User receiver = getUserById(receiverId);

        if (sender.getBalance() == null) {
            throw new RuntimeException("Sender balance is null");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    // ✅ MAPPER
    private UserResponseDTO mapToResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setBalance(user.getBalance());
        return dto;
    }
}