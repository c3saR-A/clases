package consumo.worpress.clases.services;

import java.math.BigDecimal;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String body);
    void sendWelcomeEmail(String to, String productName, Double price, String description, String filePath);
}
