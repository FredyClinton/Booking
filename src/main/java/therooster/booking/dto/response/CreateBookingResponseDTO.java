package therooster.booking.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateBookingResponseDTO {
    private UUID id;
    private Instant appointmentDate;
    private String clientNote;
    private String internalNote;
    private Double price;


}
