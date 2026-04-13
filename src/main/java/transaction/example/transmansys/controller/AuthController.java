package transaction.example.transmansys.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transaction.example.transmansys.dto.UserRequestDTO;
import transaction.example.transmansys.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}