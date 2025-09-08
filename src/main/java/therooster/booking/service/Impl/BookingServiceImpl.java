package therooster.booking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import therooster.booking.dto.request.CreateBookingRequestDTO;
import therooster.booking.dto.request.PatchBookingRequestDTO;
import therooster.booking.dto.request.UpdateBookingRequestDTO;
import therooster.booking.dto.response.CreateBookingResponseDTO;
import therooster.booking.entity.Booking;
import therooster.booking.entity.UserEntity;
import therooster.booking.enums.BookingStatus;
import therooster.booking.mapper.BookingMapper;
import therooster.booking.repository.BookingRepository;
import therooster.booking.repository.UsersRepository;
import therooster.booking.service.BookingService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UsersRepository usersRepository;
    private final BookingMapper bookingMapper;

    @Override
    public void deleteBookingById(UUID id, String username) {
        UserEntity user = this.usersRepository.findByEmail(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        // Only the owner can delete their own booking for now
        if (booking.getUser() == null || user.getId() == null
                || booking.getUser().getId() == null
                || !booking.getUser().getId().equals(user.getId())
                || user.getAuthorities().contains("ROLE_ADMIN")

        ) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User  not authorized");
        }
        this.bookingRepository.deleteById(id);
    }

    @Override
    public CreateBookingResponseDTO createBooking(CreateBookingRequestDTO dto, String username) {
        UserEntity user = this.usersRepository.findByEmail(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Booking createdBooking = new Booking(
                dto.appointmentDate(),
                dto.clientNote(),
                dto.internalNote(),
                dto.price(),
                BookingStatus.SUBMITTED,
                user);

        Booking bookingSave = this.bookingRepository.save(createdBooking);

        return bookingMapper.toDto(bookingSave);
    }

    @Override
    public CreateBookingResponseDTO getBooking(UUID id, String username) {
        UserEntity user = this.usersRepository.findByEmail(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getUser() == null || user.getId() == null || booking.getUser().getId() == null || !booking.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public CreateBookingResponseDTO updateBookingFull(UUID id, UpdateBookingRequestDTO dto, String username) {
        UserEntity user = this.usersRepository.findByEmail(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (booking.getUser() == null || user.getId() == null || booking.getUser().getId() == null || !booking.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized");
        }
        // Full replace of updatable fields
        booking.setAppointmentDate(dto.appointmentDate());
        booking.setClientNote(dto.clientNote());
        booking.setInternalNote(dto.internalNote());
        booking.setPrice(dto.price());
        if (dto.status() != null) {
            booking.setStatus(dto.status());
        }
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toDto(saved);
    }

    @Override
    public CreateBookingResponseDTO updateBookingPartial(UUID id, PatchBookingRequestDTO dto, String username) {
        UserEntity user = this.usersRepository.findByEmail(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (booking.getUser() == null || user.getId() == null || booking.getUser().getId() == null || !booking.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized");
        }
        // Partial update: only non-null fields are applied
        if (dto.appointmentDate() != null) booking.setAppointmentDate(dto.appointmentDate());
        if (dto.clientNote() != null) booking.setClientNote(dto.clientNote());
        if (dto.internalNote() != null) booking.setInternalNote(dto.internalNote());
        if (dto.price() != null) booking.setPrice(dto.price());
        if (dto.status() != null) booking.setStatus(dto.status());
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toDto(saved);
    }
}


