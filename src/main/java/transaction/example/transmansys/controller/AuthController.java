package transaction.example.transmansys.controller;

import transaction.example.transmansys.entity.User;
import transaction.example.transmansys.dto.UserRequestDTO;
import transaction.example.transmansys.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public String register(@Valid @RequestBody UserRequestDTO dto) {

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        // ✅ Use DTO balance OR default
        if (dto.getBalance() != null) {
            user.setBalance(BigDecimal.valueOf(dto.getBalance()));
        } else {
            user.setBalance(BigDecimal.valueOf(1000)); // fallback
        }

        user.setRole("USER");

        return authService.register(user);
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public String login(@RequestBody UserRequestDTO dto) {
        return authService.login(dto.getEmail(), dto.getPassword());
    }
}