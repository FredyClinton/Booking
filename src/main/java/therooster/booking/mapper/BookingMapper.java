package therooster.booking.mapper;

import org.mapstruct.Mapper;
import therooster.booking.dto.request.CreateBookingRequestDTO;
import therooster.booking.dto.response.CreateBookingResponseDTO;
import therooster.booking.entity.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toEntity(CreateBookingRequestDTO dto);

    CreateBookingResponseDTO toDto(Booking entity);

}
