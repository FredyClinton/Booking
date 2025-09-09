package therooster.booking.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import therooster.booking.dto.request.CreateBookingRequestDTO;
import therooster.booking.dto.response.CreateBookingResponseDTO;
import therooster.booking.entity.Booking;
import therooster.booking.enums.BookingStatus;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Named("stringToBookingStatus")
    static BookingStatus stringToBookingStatus(String value) {
        if (value == null) return null;
        return BookingStatus.valueOf(value.toUpperCase());
    }

    @Named("bookingStatutToString")
    static String bookingStatutToString(BookingStatus type) {
        return type == null ? null : type.name();
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "appointmentDate", source = "appointmentDate")
    @Mapping(target = "clientNote", source = "clientNote")
    @Mapping(target = "internalNote", source = "internalNote")
    @Mapping(target = "price", source = "price")
    Booking toEntity(CreateBookingRequestDTO dto);

    CreateBookingResponseDTO toDto(Booking entity);
}

