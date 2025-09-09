package therooster.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import therooster.booking.dto.request.CreateBookingRequestDTO;
import therooster.booking.dto.response.CreateBookingResponseDTO;
import therooster.booking.entity.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mappings({
            @Mapping(target = "appointmentDate", source = "appointmentDate"),
            @Mapping(target = "clientNote", source = "clientNote"),
            @Mapping(target = "internalNote", source = "internalNote"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "user", ignore = true)
    })
    Booking toEntity(CreateBookingRequestDTO dto);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "appointmentDate", source = "appointmentDate"),
            @Mapping(target = "clientNote", source = "clientNote"),
            @Mapping(target = "internalNote", source = "internalNote"),
            @Mapping(target = "price", source = "price")
    })
    CreateBookingResponseDTO toDto(Booking entity);

}
