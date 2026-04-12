package transaction.example.transmansys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import transaction.example.transmansys.dto.UserRequestDTO;
import transaction.example.transmansys.dto.UserResponseDTO;
import transaction.example.transmansys.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // CREATE
    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO dto) {
        return userService.createUser(dto);
    }

    // READ ALL
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // UPDATE ✅
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id,
                                      @RequestBody UserRequestDTO dto) {
        return userService.updateUser(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    // TRANSFER 💸
    @PostMapping("/transfer")
    public String transferMoney(@RequestParam Long senderId,
                                @RequestParam Long receiverId,
                                @RequestParam BigDecimal amount) {

        userService.transferMoney(senderId, receiverId, amount);
        return "Transaction successful";
    }
}