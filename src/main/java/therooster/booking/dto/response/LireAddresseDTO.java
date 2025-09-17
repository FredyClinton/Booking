package therooster.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LireAddresseDTO {
    private UUID id;
    private String street;    // Rue + numéro
    private String city;      // Ville
    private String postcode;  // Code postal
    private String country;   // Pays

    private Double latitude;
    private Double longitude;
}
