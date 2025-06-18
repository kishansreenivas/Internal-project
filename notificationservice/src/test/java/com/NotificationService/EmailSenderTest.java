package com.NotificationService;



import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.NotificationService.sender.EmailSender;

import static org.mockito.Mockito.*;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    private EmailSender emailSender;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void testSendEmail_Success() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String content = "Test Content";

        // Act
        emailSender.sendEmail(to, subject, content);

        // Assert
        verify(mailSender, times(1)).send(Mockito.any(SimpleMailMessage.class));
    }
    
    @Test
    void testSendEmail_Failure() {
        // Arrange
        doThrow(new RuntimeException("Mail send failed"))
            .when(mailSender)
            .send(Mockito.any(SimpleMailMessage.class));

        try {
            emailSender.sendEmail("fail@example.com", "Failure", "This will fail");
        } catch (Exception e) {
            // Expected behavior, or you can assert the exception
        }

        verify(mailSender, times(1)).send(Mockito.any(SimpleMailMessage.class));
    }

}
