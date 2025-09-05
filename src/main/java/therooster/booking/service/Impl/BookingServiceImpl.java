package therooster.booking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import therooster.booking.dto.request.CreateBookingRequestDTO;
import therooster.booking.dto.response.CreateBookingResponseDTO;
import therooster.booking.entity.Booking;
import therooster.booking.enums.BookingStatus;
import therooster.booking.mapper.BookingMapper;
import therooster.booking.repository.BookingRepository;
import therooster.booking.service.BookingService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public void deleteBookingById(UUID id) {
        this.bookingRepository.deleteById(id);
    }

    @Override
    public CreateBookingResponseDTO createBooking(CreateBookingRequestDTO dto) {
        Booking createdBooking = new Booking(

                dto.appointmentDate(),
                dto.clientNote(),
                dto.internalNote(),
                dto.price(),
                BookingStatus.SUBMITTED);

        Booking bookingSave = this.bookingRepository.save(createdBooking);

        return bookingMapper.toDto(bookingSave);
    }

    @Override
    public CreateBookingResponseDTO getBooking(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        return bookingMapper.toDto(booking);
    }
}
