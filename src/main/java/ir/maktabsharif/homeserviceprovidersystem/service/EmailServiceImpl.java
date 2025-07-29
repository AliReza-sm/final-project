package ir.maktabsharif.homeserviceprovidersystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendActivationEmail(String to, String token) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            String htmlContent = buildHtmlEmail(token);
            helper.setText(htmlContent, true);
            helper.setTo(to);
            helper.setSubject("Verify your account in home service");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildHtmlEmail(String token) {
        String activationLink = "http://localhost:8081/api/auth/activate?token=" + token;
        return activationLink;
//        return "<!DOCTYPE html>" +
//               "<html lang=\"en\">" +
//               "<head>" +
//               "<meta charset=\"UTF-8\">" +
//               "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
//               "<style>" +
//               "  body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
//               "  .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }" +
//               "  .header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #eeeeee; }" +
//               "  .header h1 { margin: 0; color: #333333; }" +
//               "  .content { padding: 20px 0; color: #555555; line-height: 1.6; }" +
//               "  .content p { margin: 0 0 15px 0; }" +
//               "  .button-container { text-align: center; padding: 20px 0; }" +
//               "  .button { background-color: #007bff; color: #ffffff; padding: 12px 25px; text-decoration: none; border-radius: 5px; display: inline-block; }" +
//               "  .footer { text-align: center; font-size: 12px; color: #999999; padding-top: 20px; border-top: 1px solid #eeeeee; }" +
//               "</style>" +
//               "</head>" +
//               "<body>" +
//               "  <div class=\"container\">" +
//               "    <div class=\"header\">" +
//               "      <h1>Welcome to Home Service!</h1>" +
//               "    </div>" +
//               "    <div class=\"content\">" +
//               "      <p>Thank you for registering. Please click the button below to activate your account.</p>" +
//               "      <p>This link will expire in one hour.</p>" +
//               "    </div>" +
//               "    <div class=\"button-container\">" +
//               "      <a href=\"" + activationLink + "\" class=\"button\">Activate Account</a>" +
//               "    </div>" +
//               "    <div class=\"footer\">" +
//               "      <p>If you did not register for this account, please ignore this email.</p>" +
//               "    </div>" +
//               "  </div>" +
//               "</body>" +
//               "</html>";
    }
}
