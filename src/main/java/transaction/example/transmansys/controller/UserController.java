package transaction.example.transmansys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import transaction.example.transmansys.entity.User;
import transaction.example.transmansys.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }


    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }


    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }


    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    // 🔥 MONEY TRANSFER API
    @PostMapping("/transfer")
    public String transferMoney(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam BigDecimal amount) {

        userService.transferMoney(senderId, receiverId, amount);
        return "Transaction successful";
    }
}