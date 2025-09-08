package therooster.booking.dto.request;

import therooster.booking.enums.BookingStatus;

import java.time.Instant;

/**
 * Partial update DTO: any null field will be ignored.
 */
public record PatchBookingRequestDTO(
        Instant appointmentDate,
        String clientNote,
        String internalNote,
        Double price,
        BookingStatus status
) {}
