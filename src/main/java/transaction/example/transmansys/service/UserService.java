package transaction.example.transmansys.service;
import transaction.example.transmansys.dto.RegisterRequestDTO;
import transaction.example.transmansys.entity.User;
import transaction.example.transmansys.repository.UserRepository;
import transaction.example.transmansys.dto.UserResponseDTO;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ CREATE USER
    public UserResponseDTO createUser(RegisterRequestDTO dto) {

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        if (dto.getBalance() != null) {
            user.setBalance(BigDecimal.valueOf(dto.getBalance()));
        } else {
            user.setBalance(BigDecimal.ZERO);
        }

        User savedUser = userRepository.save(user);

        return mapToDTO(savedUser); // 🔥 FIX
    }

    // ✅ GET ALL USERS
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO) // 🔥 FIX
                .collect(Collectors.toList());
    }

    // ✅ GET USER BY ID
    public UserResponseDTO getUserResponseById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToDTO(user); // 🔥 FIX
    }

    // ✅ UPDATE USER
    public UserResponseDTO updateUser(Long id, RegisterRequestDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        if (dto.getBalance() != null) {
            user.setBalance(BigDecimal.valueOf(dto.getBalance()));
        }

        User updatedUser = userRepository.save(user);

        return mapToDTO(updatedUser); // 🔥 FIX
    }

    // ✅ DELETE USER
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 🔥 CORE MAPPING METHOD
    private UserResponseDTO mapToDTO(User user) {

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setBalance(user.getBalance());

        return dto;
    }
}