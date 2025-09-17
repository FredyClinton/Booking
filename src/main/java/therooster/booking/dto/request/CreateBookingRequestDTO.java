package therooster.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import therooster.booking.entity.Addresse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequestDTO {
    private Instant appointmentDate;
    private String clientNote;
    private String internalNote;
    // Identifiants des services à associer à la réservation lors de la création
    private List<UUID> serviceIds;
    private Addresse addresse;
}
