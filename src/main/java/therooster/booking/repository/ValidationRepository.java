package therooster.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import therooster.booking.entity.Validation;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ValidationRepository extends JpaRepository<Validation, UUID> {
    Optional<Validation> findByCode(String code);

    void deleteAllByExpirationBefore(Instant now);
}
