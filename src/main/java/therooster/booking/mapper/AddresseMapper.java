package therooster.booking.mapper;

import org.mapstruct.Mapper;
import therooster.booking.dto.response.LireAddresseDTO;
import therooster.booking.entity.Addresse;

@Mapper(componentModel = "spring")
public interface AddresseMapper {

    LireAddresseDTO toLireAddressDTO(Addresse addresse);
}
