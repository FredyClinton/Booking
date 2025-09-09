package therooster.booking.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import therooster.booking.dto.request.CreateUserRequestDTO;
import therooster.booking.dto.response.LireUserDTO;
import therooster.booking.entity.Client;
import therooster.booking.entity.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserEntityMapper {

    // Simple names match; MapStruct can map automatically.
    LireUserDTO toDto(UserEntity entity);

    // Create a Client from sign-up data manually to avoid unmapped target issues
    default Client toClientEntity(CreateUserRequestDTO dto) {
        if (dto == null) return null;
        Client c = new Client();
        c.setFirstName(dto.getFirstName());
        c.setLastName(dto.getLastName());
        c.setEmail(dto.getEmail());
        c.setPhone(dto.getPhone());
        c.setBirthDate(dto.getBirthDate());
        // password, role, actif, timestamps, bookings, id are managed elsewhere
        return c;
    }
}
