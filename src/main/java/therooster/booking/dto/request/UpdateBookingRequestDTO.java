package therooster.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import therooster.booking.enums.BookingStatus;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateBookingRequestDTO {
    private Instant appointmentDate;
    private String clientNote;
    private String internalNote;
    private BookingStatus status;
}
