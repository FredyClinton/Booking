package therooster.booking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import therooster.booking.entity.Validation;
import therooster.booking.service.NotificationService;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendValidationEmail(Validation validation) {
        System.out.println("sendMail");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("no-reply@therooster.com");
        mailMessage.setTo(validation.getUser().getEmail());
        mailMessage.setSubject("Votre code d'activation");

        String content = String.format("Bonjour %s, <br/> vote code d'activation est  %s",
                validation.getUser().getFirstName(), validation.getCode());

        mailMessage.setText(content);
        System.out.println("sent content");
        javaMailSender.send(mailMessage);
        System.out.println("mail envoyé");
    }
}
