package transaction.example.transmansys;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "transaction.example.transmansys.repository")
@EntityScan(basePackages = "transaction.example.transmansys.entity")

public class TransmansysApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransmansysApplication.class, args);
	}

}
