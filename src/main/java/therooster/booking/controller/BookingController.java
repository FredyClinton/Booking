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
        String bookingId = bookingCreate.getId().toString();
        URI bookingUrl = ucb
                .path("/api/bookings/{bookingId}")
                .buildAndExpand(bookingId)
                .toUri();
        return ResponseEntity.created(bookingUrl).build();

    }

    @GetMapping("/{bookingId}")
    ResponseEntity<CreateBookingResponseDTO> getBooking(@PathVariable UUID bookingId) {
        String username = getPrincipalUsername();
        return ResponseEntity.ok(this.bookingService.getBooking(bookingId, username));
    }


    @DeleteMapping("/{bookingId}")
    ResponseEntity<Void> deleteBooking(@PathVariable UUID bookingId) {
        String username = getPrincipalUsername();
        // Admin-only deletion; service enforces role
        this.bookingService.deleteBookingById(bookingId, username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{bookingId}")
    ResponseEntity<CreateBookingResponseDTO> updateBookingFull(@PathVariable UUID bookingId,
                                                               @RequestBody UpdateBookingRequestDTO dto) {
        String username = getPrincipalUsername();
        CreateBookingResponseDTO updated = this.bookingService.updateBookingFull(bookingId, dto, username);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{bookingId}")
    ResponseEntity<CreateBookingResponseDTO> updateBookingPartial(@PathVariable UUID bookingId,
                                                                  @RequestBody PatchBookingRequestDTO dto) {
        String username = getPrincipalUsername();
        CreateBookingResponseDTO updated = this.bookingService.updateBookingPartial(bookingId, dto, username);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{bookingId}/request-deletion")
    ResponseEntity<CreateBookingResponseDTO> requestDeletion(@PathVariable UUID bookingId) {

        CreateBookingResponseDTO dto = this.bookingService.requestDeletion(bookingId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    ResponseEntity<List<CreateBookingResponseDTO>> listMyBookings() {

        return ResponseEntity.ok(this.bookingService.getAllBookings());
    }

}
