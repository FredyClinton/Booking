package therooster.booking.mapper;

import org.mapstruct.*;
import therooster.booking.dto.request.CreateBookingRequestDTO;
import therooster.booking.dto.response.CreateBookingResponseDTO;
import therooster.booking.entity.Booking;
import therooster.booking.enums.BookingStatus;

@Mapper(componentModel = "spring", uses = {ServiceMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
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
    @Mapping(target = "addresse", source = "addresse")
        // services will be set in service layer using serviceIds
    Booking toEntity(CreateBookingRequestDTO dto);

    @Mapping(target = "services", source = "services")
    CreateBookingResponseDTO toDto(Booking entity);
}

