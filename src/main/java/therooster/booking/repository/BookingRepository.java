package therooster.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import therooster.booking.entity.Booking;

import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

}
