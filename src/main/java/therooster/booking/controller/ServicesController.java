package therooster.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import therooster.booking.dto.request.CreateServiceDTO;
import therooster.booking.dto.response.LireServiceDTO;
import therooster.booking.service.ServicesService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/services")
@RequiredArgsConstructor
public class ServicesController {

    private final ServicesService servicesService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LireServiceDTO> create(@RequestBody CreateServiceDTO dto) {
        LireServiceDTO created = servicesService.createService(dto);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        servicesService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LireServiceDTO>> list() {
        return ResponseEntity.ok(servicesService.listServices());
    }
}
