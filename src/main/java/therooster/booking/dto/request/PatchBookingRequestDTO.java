package therooster.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import therooster.booking.enums.BookingStatus;

import java.time.Instant;

/**
 * Partial update DTO: any null field will be ignored.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatchBookingRequestDTO {
    private Instant appointmentDate;
    private String clientNote;
    private String internalNote;
    private Double price;
    private BookingStatus status;
}
