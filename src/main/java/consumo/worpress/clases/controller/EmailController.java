package consumo.worpress.clases.controller;

import consumo.worpress.clases.services.EmailService;
import jakarta.annotation.Generated;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService){
        this.emailService = emailService;
    }

    @GetMapping("/test-email")
    public String testEmail() {
        emailService.sendSimpleEmail(
                "06cesar.leon@gmail.com",
                "Palabras Sagradas NIGGA!",
                "Wassup, can a loc come up in your crib?" +
                        "Man, fuck you. I'll see you at work." +
                        "Ah, nigga, don't hate me cause I'm beautiful, nigga." +
                        "Maybe if you got rid of that old yee-yee-ass haircut you got," +
                        "you'd get some bitches on your dick." +
                        "Oh, better yet, maybe Tanisha'll call your dog-ass" +
                        "if she ever stop fucking with that brain surgeon or lawyer she fucking with." +
                        "Nigga..." +
                        "\n" +
                        "What?!");
        return "Email Enviado Crack";
    }

    @GetMapping("/test-email-html")
    public String testEmailHTML() {
        emailService.sendWelcomeEmail(
                "david.melgar@colibrihub.com",
                "Algo Cool",
                173.00 ,
                "una descripción",
                "/home/Andrades/Documentos/Web/sesion_7.pdf"
        );
        return "Email Enviado Crack";
    }

}
