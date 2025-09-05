package therooster.booking.mapper;

import org.mapstruct.Mapper;
import therooster.booking.dto.request.CreateUserRequestDTO;
import therooster.booking.dto.response.LireUserDTO;
import therooster.booking.entity.Client;
import therooster.booking.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    LireUserDTO toDto(UserEntity entity);

    Client toClientEntity(CreateUserRequestDTO dto);
}
