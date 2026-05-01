package itm.proyectoharoldo.backend.Interfaces;

public interface IEmailService {
    void sendWelcomeEmail(String toEmail, String toName);
    void sendAdviserWelcomeEmail(String toEmail, String toName);
    void sendAdviserAccountAuthorizedEmail(String toEmail, String toName);
    void sendAccountDeletedEmail(String toEmail, String toName);
}
