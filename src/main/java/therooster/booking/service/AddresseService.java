package therooster.booking.service;

import therooster.booking.dto.request.GeocodingForwardRequestDTO;
import therooster.booking.dto.response.LireAddresseDTO;
import therooster.booking.entity.Addresse;

import java.util.List;
import java.util.UUID;

public interface AddresseService {
    LireAddresseDTO saveAddresse(GeocodingForwardRequestDTO geocodingForwardRequestDTO);

    void deleteAddresse(UUID addresseId);

    LireAddresseDTO updateAddresse(Addresse addresse);

    /**
     * Retourne la liste des addresses créees par l'utilisateur connecté
     *
     */
    List<LireAddresseDTO> listAddresse();

    LireAddresseDTO getAddressById(UUID addresseId);
}
