package therooster.booking.service;

import therooster.booking.dto.request.CreateBookingRequestDTO;
import therooster.booking.dto.request.PatchBookingRequestDTO;
import therooster.booking.dto.request.UpdateBookingRequestDTO;
import therooster.booking.dto.response.CreateBookingResponseDTO;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    void deleteBookingById(UUID id, String username);

    CreateBookingResponseDTO createBooking(CreateBookingRequestDTO dto, String username);

    CreateBookingResponseDTO getBooking(UUID id, String username);

    CreateBookingResponseDTO updateBookingFull(UUID id, UpdateBookingRequestDTO dto, String username);

    CreateBookingResponseDTO updateBookingPartial(UUID id, PatchBookingRequestDTO dto, String username);

    List<CreateBookingResponseDTO> getAllBookings(String username);
}
