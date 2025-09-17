package therooster.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeocodingForwardRequestDTO {
    private String street;     // Rue + numéro
    private String city;       // Ville
    private String postcode;   // Code postal
    private String country;    // Pays
}
