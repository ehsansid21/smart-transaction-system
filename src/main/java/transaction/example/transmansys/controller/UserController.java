package transaction.example.transmansys.controller;

import transaction.example.transmansys.dto.RegisterRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import transaction.example.transmansys.dto.UserResponseDTO;
import transaction.example.transmansys.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ CREATE USER
    @PostMapping
    public UserResponseDTO createUser(@RequestBody @Valid RegisterRequestDTO dto) {
        return userService.createUser(dto);
    }

    // ✅ GET ALL USERS
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ GET USER BY ID
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserResponseById(id);
    }

    // ✅ UPDATE USER
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @RequestBody @Valid RegisterRequestDTO dto) {

        return userService.updateUser(id, dto);
    }

    // ✅ DELETE USER
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }
}