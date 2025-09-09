package therooster.booking.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import therooster.booking.entity.UserEntity;
import therooster.booking.entity.Validation;
import therooster.booking.repository.ValidationRepository;
import therooster.booking.service.ValidationService;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ValidationServiceImpl implements ValidationService {
    private final ValidationRepository validationRepository;
    private final NotificationServiceImpl notificationService;


    @Override
    public void enregistrer(UserEntity utilisateur) {
        System.out.println("validation");
        Validation validation = new Validation();
        validation.setUser(utilisateur);


        Instant creation = Instant.now();
        validation.setCreation(creation);
        var expiration = creation.plus(30, MINUTES);
        validation.setExpiration(expiration);

        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);
        validation.setCode(code);

        this.validationRepository.save(validation);
        this.notificationService.sendValidationEmail(validation);
    }

    @Override
    public Validation lireEnFonctionDuCode(String code) {
        return this.validationRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Code invalide"));

    }

    @Override
    public void removeUseLessJwt() {

    }
}
