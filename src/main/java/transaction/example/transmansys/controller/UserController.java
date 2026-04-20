package transaction.example.transmansys.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import transaction.example.transmansys.dto.UserRequestDTO;
import transaction.example.transmansys.dto.UserResponseDTO;
import transaction.example.transmansys.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET CURRENT USER PROFILE (any authenticated user)
    @GetMapping("/me")
    public UserResponseDTO getMyProfile(Authentication auth) {
        return userService.getUserByEmail(auth.getName());
    }

    // CREATE USER (public or admin only if you want later)
    @PostMapping
    public UserResponseDTO createUser(@RequestBody @Valid UserRequestDTO dto) {
        return userService.createUser(dto);
    }

    // GET ALL USERS → ADMIN ONLY
    @GetMapping
    public List<UserResponseDTO> getAllUsers(Authentication auth) {
        return userService.getAllUsers(auth);
    }

    // GET USER BY ID → OWNER OR ADMIN ONLY
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id, Authentication auth) {
        return userService.getUserById(id, auth);
    }

    // UPDATE USER → OWNER ONLY (or admin)
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserRequestDTO dto,
            Authentication auth) {

        return userService.updateUser(id, dto, auth);
    }

    // DEACTIVATE USER → ADMIN ONLY
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id, Authentication auth) {
        userService.deleteUser(id, auth);
        return "User deactivated successfully";
    }

    // ACTIVATE USER → ADMIN ONLY
    @PutMapping("/{id}/activate")
    public String activateUser(@PathVariable Long id, Authentication auth) {
        userService.activateUser(id, auth);
        return "User activated successfully";
    }
}