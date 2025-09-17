package therooster.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import therooster.booking.dto.request.GeocodingForwardRequestDTO;
import therooster.booking.dto.response.LireAddresseDTO;
import therooster.booking.service.Impl.AddresseServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/addresses")
@RequiredArgsConstructor
public class AddresseController {
    private final AddresseServiceImpl addresseService;

    @GetMapping
    public ResponseEntity<List<LireAddresseDTO>> getAllAddresses() {
        return ResponseEntity.ok(this.addresseService.listAddresse());
    }

    @GetMapping("/{addresseId}")
    public ResponseEntity<LireAddresseDTO> getAddress(@PathVariable UUID addresseId) {
        return ResponseEntity.ok(this.addresseService.getAddressById(addresseId));
    }

    @PostMapping
    public ResponseEntity<LireAddresseDTO> createAddresse(@RequestBody GeocodingForwardRequestDTO forwardRequestDTO) {
        LireAddresseDTO savedAddress = this.addresseService.saveAddresse(forwardRequestDTO);

        // TODO : Arranger
        return ResponseEntity.ok(savedAddress);
    }

    @PutMapping
    public ResponseEntity<LireAddresseDTO> updateAddresse(@RequestBody GeocodingForwardRequestDTO forwardRequestDTO) {
        LireAddresseDTO savedAddress = this.addresseService.saveAddresse(forwardRequestDTO);

        // TODO : Arranger
        return ResponseEntity.ok(savedAddress);
    }

    @DeleteMapping("/{addresseId}")
    public ResponseEntity<Void> deleteAddresse(@PathVariable UUID addresseId) {
        this.addresseService.deleteAddresse(addresseId);
        return ResponseEntity.noContent().build();
    }
}
