package therooster.booking.service.Impl;

import com.opencagedata.jopencage.model.JOpenCageLatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import therooster.booking.config.geoconding.GeoCodingService;
import therooster.booking.config.security.SecurityUtils;
import therooster.booking.dto.request.GeocodingForwardRequestDTO;
import therooster.booking.dto.response.LireAddresseDTO;
import therooster.booking.entity.Addresse;
import therooster.booking.mapper.AddresseMapper;
import therooster.booking.repository.AddresseRepository;
import therooster.booking.service.AddresseService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddresseServiceImpl implements AddresseService {

    private final AddresseRepository addresseRepository;
    private final GeoCodingService geoCodingService;
    private final AddresseMapper addresseMapper;


    @Override
    public LireAddresseDTO saveAddresse(GeocodingForwardRequestDTO forwardRequestDTO) {
        Optional<Addresse> existAddress = this.addresseRepository.findByStreetAndCityAndCountryAndPostcode(
                forwardRequestDTO.getStreet(),
                forwardRequestDTO.getCity(),
                forwardRequestDTO.getCountry(),
                forwardRequestDTO.getPostcode()
        );
        if (existAddress.isPresent()) {

            return this.addresseMapper.toLireAddressDTO(existAddress.get());
        } else {

            Addresse addresse = new Addresse();
            addresse.setStreet(forwardRequestDTO.getStreet());
            addresse.setCity(forwardRequestDTO.getCity());
            addresse.setCountry(forwardRequestDTO.getCountry());
            addresse.setPostcode(forwardRequestDTO.getPostcode());
            addresse.setCreatedBy(SecurityUtils.getCurrentUser());

            JOpenCageLatLng latLng = this.geoCodingService.forwardGeocode(addresse.getFormattedAddress(), null);
            addresse.setLatitude(latLng.getLat());
            addresse.setLongitude(latLng.getLng());
            Addresse savedAddresse = this.addresseRepository.save(addresse);
            return this.addresseMapper.toLireAddressDTO(savedAddresse);

        }

    }

    @Override
    public void deleteAddresse(UUID addresseId) {
        Optional<Addresse> addresse = this.addresseRepository.findById(addresseId);
        if (addresse.isPresent()) {
            this.addresseRepository.deleteById(addresseId);
        } else {
            throw new IllegalArgumentException("Addresse not found");
        }
    }

    @Override
    public LireAddresseDTO updateAddresse(Addresse addresse) {
        if (addresse != null && SecurityUtils.currentUserHasRole("ROLE_ADMIN")) {
            Addresse savedAddresse = this.addresseRepository.save(addresse);
            return this.addresseMapper.toLireAddressDTO(savedAddresse);
        } else {
            throw new IllegalArgumentException("L'adresse est null");
        }

    }

    @Override
    public List<LireAddresseDTO> listAddresse() {
        List<Addresse> addresses;
        if (SecurityUtils.currentUserHasRole("ROLE_ADMIN")) {
            addresses = this.addresseRepository.findAll();
        }

        addresses = this.addresseRepository.findAllByCreatedBy_Email(SecurityUtils.getCurrentUsername());
        return addresses.stream()
                .map(this.addresseMapper::toLireAddressDTO)
                .toList();
    }

    @Override
    public LireAddresseDTO getAddressById(UUID addresseId) {
        Addresse addresse = this.addresseRepository.findById(addresseId).orElseThrow(
                () -> new IllegalArgumentException("Addresse not found")
        );

        return this.addresseMapper.toLireAddressDTO(addresse);

    }


}
