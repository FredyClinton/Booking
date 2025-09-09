package therooster.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequestDTO {
    private Instant appointmentDate;
    private String clientNote;
    private String internalNote;
    private Double price;


}
