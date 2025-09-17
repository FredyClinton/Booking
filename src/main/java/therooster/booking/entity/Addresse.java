package therooster.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;


@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Addresse extends BaseIdUuid {

    private String street;    // Rue + numéro
    private String city;      // Ville
    private String postcode;  // Code postal
    private String country;   // Pays

    private Double latitude;
    private Double longitude;


    @ManyToOne
    private UserEntity createdBy;

    /**
     * Génère une adresse formatée à partir des autres champs.
     */
    @Transient // Ne sera pas persisté en base
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();

        if (street != null && !street.isBlank()) sb.append(street).append(", ");
        if (postcode != null && !postcode.isBlank()) sb.append(postcode).append(" ");
        if (city != null && !city.isBlank()) sb.append(city).append(", ");
        if (country != null && !country.isBlank()) sb.append(country);

        return sb.toString().trim().replaceAll(", $", ""); // Nettoyage final
    }
}
