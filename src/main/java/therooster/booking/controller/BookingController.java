package therooster.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import therooster.booking.dto.request.CreateBookingRequestDTO;
import therooster.booking.dto.request.PatchBookingRequestDTO;
import therooster.booking.dto.request.UpdateBookingRequestDTO;
import therooster.booking.dto.response.CreateBookingResponseDTO;
import therooster.booking.service.Impl.BookingServiceImpl;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/bookings")

@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;

    private static String getPrincipalUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping
    ResponseEntity<Void> createBooking(
            @RequestBody CreateBookingRequestDTO dto,
            UriComponentsBuilder ucb
    ) {
        String username = getPrincipalUsername();
        CreateBookingResponseDTO bookingCreate = this.bookingService.createBooking(dto, username);
        System.out.println("creation de booking");
        var id = bookingCreate.getId().toString();
        URI bookingUrl = ucb
                .path("/api/bookings/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(bookingUrl).build();

    }

    @GetMapping("/{id}")
    ResponseEntity<CreateBookingResponseDTO> getBooking(@PathVariable UUID id) {
        String username = getPrincipalUsername();
        return ResponseEntity.ok(this.bookingService.getBooking(id, username));
    }


    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteBooking(@PathVariable UUID id) {
        String username = getPrincipalUsername();
        // Admin-only deletion; service enforces role
        this.bookingService.deleteBookingById(id, username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    ResponseEntity<CreateBookingResponseDTO> updateBookingFull(@PathVariable UUID id,
                                                               @RequestBody UpdateBookingRequestDTO dto) {
        String username = getPrincipalUsername();
        CreateBookingResponseDTO updated = this.bookingService.updateBookingFull(id, dto, username);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    ResponseEntity<CreateBookingResponseDTO> updateBookingPartial(@PathVariable UUID id,
                                                                  @RequestBody PatchBookingRequestDTO dto) {
        String username = getPrincipalUsername();
        CreateBookingResponseDTO updated = this.bookingService.updateBookingPartial(id, dto, username);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/request-deletion")
    ResponseEntity<CreateBookingResponseDTO> requestDeletion(@PathVariable UUID id) {
        String username = getPrincipalUsername();
        var dto = this.bookingService.requestDeletion(id, username);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    ResponseEntity<List<CreateBookingResponseDTO>> listMyBookings() {
        String username = getPrincipalUsername();
        return ResponseEntity.ok(this.bookingService.getAllBookings(username));
    }

}
