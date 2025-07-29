package ir.maktabsharif.homeserviceprovidersystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void sendActivationEmail() throws MessagingException, IOException {
        String sendToEmail = "ali@gmail.com";
        String activationToken = "asdgcty67de54eghg8iuh990kjby";
        String link = "http://localhost:8081/api/auth/activate?token=" + activationToken;
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService.sendActivationEmail(sendToEmail, activationToken);
        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, times(1)).send(mimeMessageCaptor.capture());
        MimeMessage sentMessage = mimeMessageCaptor.getValue();
        assertThat(sentMessage.getRecipients(MimeMessage.RecipientType.TO)[0].toString()).isEqualTo(sendToEmail);
        assertThat(sentMessage.getSubject()).isEqualTo("Verify your account in home service");
        String content = (String) sentMessage.getContent();
        assertThat(content).isNotNull();
        assertThat(content).contains(link);
    }
}