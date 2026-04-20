package transaction.example.transmansys.service;

import transaction.example.transmansys.dto.UserRequestDTO;
import transaction.example.transmansys.dto.UserResponseDTO;
import transaction.example.transmansys.entity.User;
import transaction.example.transmansys.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ================= GET PROFILE BY EMAIL (for /users/me) =================
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    // ================= CREATE USER =================
    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setBalance(dto.getBalance());
        
        if (dto.getRole() != null && !dto.getRole().isEmpty()) {
            user.setRole(dto.getRole().toUpperCase());
        } else {
            user.setRole("USER");
        }

        userRepository.save(user);
        return mapToDTO(user);
    }

    // ================= GET ALL USERS (ADMIN ONLY) =================
    public List<UserResponseDTO> getAllUsers(Authentication auth) {
        if (!isAdmin(auth)) {
            throw new AccessDeniedException("Only admin can view all users");
        }
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ================= GET USER BY ID (OWNER OR ADMIN) =================
    public UserResponseDTO getUserById(Long id, Authentication auth) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!isAdmin(auth) && !user.getEmail().equals(auth.getName())) {
            throw new AccessDeniedException("You can only access your own data");
        }
        return mapToDTO(user);
    }

    // ================= UPDATE USER =================
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto, Authentication auth) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!isAdmin(auth) && !user.getEmail().equals(auth.getName())) {
            throw new AccessDeniedException("Not allowed");
        }
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        if (dto.getBalance() != null) {
            user.setBalance(dto.getBalance());
        }
        userRepository.save(user);
        return mapToDTO(user);
    }

    // ================= DELETE (DEACTIVATE) USER =================
    public void deleteUser(Long id, Authentication auth) {
        if (!isAdmin(auth)) {
            throw new AccessDeniedException("Only admin can deactivate users");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    // ================= ACTIVATE USER =================
    public void activateUser(Long id, Authentication auth) {
        if (!isAdmin(auth)) {
            throw new AccessDeniedException("Only admin can activate users");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    // ================= HELPERS =================
    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setBalance(user.getBalance());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }
}