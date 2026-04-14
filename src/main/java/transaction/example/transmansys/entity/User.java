package transaction.example.transmansys.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @Column(nullable = false)
    private BigDecimal balance;

    private String role;

    // ===== Constructors =====
    public User() {}

    public User(String name, String email, String password, BigDecimal balance, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.role = role;
    }

    // ===== Getters =====
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getRole() {
        return role;
    }

    // ===== Setters =====
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setRole(String role) {
        this.role = role;
    }
}