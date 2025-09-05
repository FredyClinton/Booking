package therooster.booking.dto.request;

import java.time.Instant;

public record CreateBookingRequestDTO(Instant appointmentDate,
                                      String clientNote,
                                      String internalNote,
                                      Double price) {


}
