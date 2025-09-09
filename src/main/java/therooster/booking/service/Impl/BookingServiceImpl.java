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
import therooster.booking.entity.ServiceEntity;
import therooster.booking.entity.UserEntity;
import therooster.booking.enums.BookingStatus;
import therooster.booking.mapper.BookingMapper;
import therooster.booking.repository.BookingRepository;
import therooster.booking.repository.ServiceRepository;
import therooster.booking.repository.UsersRepository;
import therooster.booking.service.BookingService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UsersRepository usersRepository;
    private final ServiceRepository serviceRepository;
    private final BookingMapper bookingMapper;

    private boolean hasRole(UserEntity user, String role) {
        return user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    private boolean isAssignedToEmployee(Booking booking, String email) {
        return booking.getEmployees() != null && booking.getEmployees().stream().anyMatch(e -> email.equals(e.getEmail()));
    }

    @Override
    public void deleteBookingById(UUID id, String username) {
        UserEntity user = this.usersRepository.findByEmail(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        // Only ADMIN can delete, and optionally only if pending deletion
        if (!hasRole(user, "ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin can delete bookings");
        }
        // Admin deletes regardless of owner; if you want to enforce pending state, uncomment next lines
        // if (booking.getStatus() != BookingStatus.PENDING_DELETION) {
        //     throw new ResponseStatusException(HttpStatus.CONFLICT, "Deletion not requested");
        // }
        this.bookingRepository.deleteById(id);
    }

    @Override
    public CreateBookingResponseDTO createBooking(CreateBookingRequestDTO dto, String username) {
        UserEntity user = this.usersRepository.findByEmail(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Booking createdBooking = new Booking();
        createdBooking.setAppointmentDate(dto.getAppointmentDate());
        createdBooking.setClientNote(dto.getClientNote());
        createdBooking.setInternalNote(dto.getInternalNote());
        createdBooking.setStatus(BookingStatus.SUBMITTED);
        createdBooking.setUser(user);

        // Associer les services si des IDs sont fournis
        if (dto.getServiceIds() != null && !dto.getServiceIds().isEmpty()) {
            Set<ServiceEntity> services = new HashSet<>(serviceRepository.findAllById(dto.getServiceIds()));
            if (services.size() != dto.getServiceIds().size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un ou plusieurs services sont introuvables");
            }
            createdBooking.setServices(services);
        }

        Booking bookingSave = this.bookingRepository.save(createdBooking);

        return bookingMapper.toDto(bookingSave);
    }

    @Override
    public CreateBookingResponseDTO getBooking(UUID id, String username) {
        UserEntity user = this.usersRepository.findByEmail(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Acces non autorisé")
        );
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        boolean isOwner = booking.getUser() != null && booking.getUser().getEmail() != null && booking.getUser().getEmail().equals(user.getEmail());
        boolean isAdmin = hasRole(user, "ROLE_ADMIN");
        boolean isAssigned = isAssignedToEmployee(booking, user.getEmail());

        if (!(isOwner || isAdmin || isAssigned)) {
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
        boolean isOwner = booking.getUser() != null && booking.getUser().getEmail() != null && booking.getUser().getEmail().equals(user.getEmail());
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized");
        }
        // Full replace of updatable fields
        booking.setAppointmentDate(dto.getAppointmentDate());
        booking.setClientNote(dto.getClientNote());
        booking.setInternalNote(dto.getInternalNote());
        if (dto.getStatus() != null) {
            booking.setStatus(dto.getStatus());
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
        boolean isOwner = booking.getUser() != null && booking.getUser().getEmail() != null && booking.getUser().getEmail().equals(user.getEmail());
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized");
        }
        // Partial update: only non-null fields are applied
        if (dto.getAppointmentDate() != null) booking.setAppointmentDate(dto.getAppointmentDate());
        if (dto.getClientNote() != null) booking.setClientNote(dto.getClientNote());
        if (dto.getInternalNote() != null) booking.setInternalNote(dto.getInternalNote());
        if (dto.getStatus() != null) booking.setStatus(dto.getStatus());
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toDto(saved);
    }

    @Override
    public List<CreateBookingResponseDTO> getAllBookings(String username) {
        UserEntity user = this.usersRepository.findByEmail(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        List<Booking> bookings;
        if (hasRole(user, "ROLE_ADMIN")) {
            bookings = bookingRepository.findAll();
        } else if (hasRole(user, "ROLE_EMPLOYEE")) {
            bookings = bookingRepository.findDistinctByEmployees_Email(user.getEmail());
        } else {
            bookings = bookingRepository.findByUser_Email(user.getEmail());
        }

        return bookings.stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    // Employee requests deletion -> mark as PENDING_DELETION
    public CreateBookingResponseDTO requestDeletion(UUID id, String username) {
        UserEntity user = this.usersRepository.findByEmail(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (!hasRole(user, "ROLE_EMPLOYEE") || !isAssignedToEmployee(booking, user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to request deletion");
        }
        booking.setStatus(BookingStatus.PENDING_DELETION);
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toDto(saved);
    }
}


