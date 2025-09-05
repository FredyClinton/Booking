package therooster.booking.service;

import therooster.booking.dto.request.CreateBookingRequestDTO;
import therooster.booking.dto.response.CreateBookingResponseDTO;

import java.util.UUID;

public interface BookingService {
    void deleteBookingById(UUID id);

    CreateBookingResponseDTO createBooking(CreateBookingRequestDTO dto);

    CreateBookingResponseDTO getBooking(UUID id);
}
