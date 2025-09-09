package therooster.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import therooster.booking.entity.ServiceEntity;

import java.util.UUID;

public interface ServiceRepository extends JpaRepository<ServiceEntity, UUID> {
}
