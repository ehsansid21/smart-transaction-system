package transaction.example.transmansys.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    /**
     * MOCK EMAIL SENDER
     * In a real production environment, this would use JavaMailSender to send an actual email via SMTP.
     * For this project, it simulates sending an email by printing a formatted receipt to the console,
     * demonstrating the architectural pattern of third-party integration without requiring real credentials.
     */
    public void sendTransferReceipt(String toEmail, String senderName, String receiverName, BigDecimal amount) {
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        System.out.println("\n=======================================================");
        System.out.println("📧 EMAIL DISPATCHED TO: " + toEmail);
        System.out.println("=======================================================");
        System.out.println("Subject: Your TransactX Transfer Receipt");
        System.out.println("From: no-reply@transactx.com\n");
        
        System.out.println("Hello " + senderName + ",\n");
        System.out.println("Your transfer was successful! Here are the details:\n");
        System.out.println("  Amount Sent:   ₹" + amount);
        System.out.println("  Sent To:       " + receiverName);
        System.out.println("  Date/Time:     " + timestamp);
        System.out.println("\nThank you for using TransactX.");
        System.out.println("=======================================================\n");
    }
}
