package transaction.example.transmansys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import transaction.example.transmansys.entity.User;
import transaction.example.transmansys.service.UserService;

@RestController
@RequestMapping("/api/users")
public class TestController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }
}