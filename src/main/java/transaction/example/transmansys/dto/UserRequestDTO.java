package transaction.example.transmansys.dto;

import jakarta.validation.constraints.*;

public class UserRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    private String email;

    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    @NotNull(message = "Balance required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be >= 0")
    private Double balance;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}