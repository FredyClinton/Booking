package therooster.booking.service;

import therooster.booking.entity.Validation;

public interface NotificationService {
    void sendValidationEmail(Validation validation);
}
