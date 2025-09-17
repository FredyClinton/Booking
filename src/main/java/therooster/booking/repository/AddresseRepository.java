package therooster.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import therooster.booking.entity.Addresse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddresseRepository extends JpaRepository<Addresse, UUID> {

    Optional<Addresse> findByStreetAndCityAndCountryAndPostcode(String street, String city, String country, String postcode);

    //Optional<Addresse> findByLatitudeAndLongitude(Double latitude, Double longitude);

    // boolean existByStreetAndCityAndCountryAndPostcode(String street, String city, String country, String postcode);

    List<Addresse> findAllByCreatedBy_Email(String email);
}
