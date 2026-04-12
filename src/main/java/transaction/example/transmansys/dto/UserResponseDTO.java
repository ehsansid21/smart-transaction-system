package transaction.example.transmansys.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private BigDecimal balance;
}