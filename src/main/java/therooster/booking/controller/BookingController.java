package therooster.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import therooster.booking.dto.request.CreateBookingRequestDTO;
import therooster.booking.dto.response.CreateBookingResponseDTO;
import therooster.booking.service.Impl.BookingServiceImpl;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "bookings")

@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PostMapping
    ResponseEntity<Void> createBooking(
            @RequestBody CreateBookingRequestDTO dto,
            UriComponentsBuilder ucb) {
        CreateBookingResponseDTO bookingCreate = this.bookingService.createBooking(dto);

        URI bookingUrl = ucb
                .path("api/bookings/{id}")
                .buildAndExpand(bookingCreate.id())
                .toUri();
        return ResponseEntity.created(bookingUrl).build();

    }

    @GetMapping("/{id}")
    ResponseEntity<CreateBookingResponseDTO> getBooking(@PathVariable UUID id) {
        return ResponseEntity.ok(this.bookingService.getBooking(id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteBooking(@PathVariable UUID id) {
        CreateBookingResponseDTO booking = this.bookingService.getBooking(id);

        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        this.bookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build();
    }


}
