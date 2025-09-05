package therooster.booking.service;

import therooster.booking.entity.UserEntity;
import therooster.booking.entity.Validation;

public interface ValidationService {
    void enregistrer(UserEntity utilisateur);

    Validation lireEnFonctionDuCode(String code);

    void removeUseLessJwt();
}
