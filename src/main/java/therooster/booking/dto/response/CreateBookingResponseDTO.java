package therooster.booking.dto.response;


import java.time.Instant;
import java.util.UUID;

public record CreateBookingResponseDTO(
        UUID id,
        Instant appointmentDate,
        String clientNote,
        String internalNote,
        Double price
) {

}
