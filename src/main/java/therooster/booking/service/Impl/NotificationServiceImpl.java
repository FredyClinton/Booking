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
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("no-reply@therooster.com");
        mailMessage.setTo(validation.getUtilisateur().getEmail());
        mailMessage.setSubject("Votre code d'activation");

        String content = String.format("Bonjour %s, <br/> vote code d'activation est  %s",
                validation.getUtilisateur().getFirstName(), validation.getCode());

        mailMessage.setText(content);

        javaMailSender.send(mailMessage);
    }
}
