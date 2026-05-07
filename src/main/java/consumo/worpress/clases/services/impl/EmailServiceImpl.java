package consumo.worpress.clases.services.impl;

import consumo.worpress.clases.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.math.BigDecimal;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email.from:onboarding@resend.dev}")
    private String fromEmail;
    @Value("${app.email.subject:onboarding@resend.dev}")
    private String subject;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine){
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendSimpleEmail(String to, String subject, String body) {
        log.info("Sending Email to '{}' with subject '{}'", to, subject);
        log.info("DEBUG - From: [{}]", fromEmail);
        log.info("DEBUG - To: [{}]", to);

        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(fromEmail);

            mailSender.send(message);
            log.info("Email sent succesfully to {}", to);
        } catch (Exception e){
            log.error("FALLO CRÍTICO: ", e); // Imprime el stacktrace completo
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendWelcomeEmail(String to, String productName, Double price, String description, String filePath) {
        log.info("Enviando email de bienvenida a: {}", to);
        try{
            Context context = new Context();
            context.setVariable("productName", productName);
            context.setVariable("price", price);
            context.setVariable("description", description);

            // Renderizar template
            String htmlContent = templateEngine.process("email/welcome", context);

            // Crear mensaje MIME (permite HTML)
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = es HTML

            if (filePath != null && !filePath.isEmpty()) {
                FileSystemResource file = new FileSystemResource(new File(filePath));
                if (file.exists()) {
                    // Se toma el nombre real del archivo para el adjunto
                    helper.addAttachment(file.getFilename(), file);
                    log.info("Archivo adjunto añadido: {}", file.getFilename());
                } else {
                    log.warn("No se encontró el archivo en la ruta: {}", filePath);
                }
            }

            mailSender.send(message);
            log.info("Email HTML enviado exitosamente a {}", to);
        }
        catch (Exception e){
            log.error("Error while sendidn email to {}", to, e);
        }
    }
}
