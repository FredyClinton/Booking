package therooster.booking.mapper;

import org.mapstruct.Mapper;
import therooster.booking.dto.request.CreateBookingDTO;
import therooster.booking.entity.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toEntity(CreateBookingDTO dto);

}
