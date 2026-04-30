package itm.proyectoharoldo.backend.Services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GmailEmailService {

    private static final Logger logger = LoggerFactory.getLogger(GmailEmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail, String toName) {
        String subject = "¡Bienvenido a Haroldo Finanzas!";
        String html = loadHtmlTemplate("WelcomeMailContent.html")
                .replace("{{name}}", toName)
                .replace("{{email}}", toEmail);
        sendEmail(toEmail, subject, html);
    }

    public void sendAdviserWelcomeEmail(String toEmail, String toName) {
        String subject = "¡Bienvenido a Haroldo Finanzas!";
        String html = loadHtmlTemplate("AdviserWelcomeMailContent.html")
                .replace("{{name}}", toName)
                .replace("{{email}}", toEmail);
        sendEmail(toEmail, subject, html);
    }

    public void sendAdviserAccountAuthorizedEmail(String toEmail, String toName) {
        String subject = "Su cuenta ha sido autorizada - Haroldo Finanzas";
        String html = loadHtmlTemplate("AdviserAccountAuthorizedMailContent.html")
                .replace("{{name}}", toName)
                .replace("{{email}}", toEmail);
        sendEmail(toEmail, subject, html);
    }

    public void sendAccountDeletedEmail(String toEmail, String toName) {
        String subject = "Su cuenta fue eliminada - Haroldo Finanzas";
        String html = loadHtmlTemplate("AccountDeletedMailContent.html")
                .replace("{{name}}", toName)
                .replace("{{email}}", toEmail)
                .replace("{{date}}", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        sendEmail(toEmail, subject, html);
    }

    @SuppressWarnings("null")
    private void sendEmail(String toEmail, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Email sent successfully to {}", toEmail);
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }

    private String loadHtmlTemplate(String templateName) {
        try {
            ClassPathResource resource = new ClassPathResource("HtmlTemplates/" + templateName);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Failed to load template {}: {}", templateName, e.getMessage());
            throw new RuntimeException("Error al cargar plantilla: " + templateName);
        }
    }

}
