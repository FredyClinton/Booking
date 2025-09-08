package therooster.booking.dto.request;

import therooster.booking.enums.BookingStatus;

import java.time.Instant;

public record UpdateBookingRequestDTO(
        Instant appointmentDate,
        String clientNote,
        String internalNote,
        Double price,
        BookingStatus status
) {}
