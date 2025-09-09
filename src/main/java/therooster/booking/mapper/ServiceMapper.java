package therooster.booking.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import therooster.booking.dto.request.CreateServiceDTO;
import therooster.booking.dto.response.LireServiceDTO;
import therooster.booking.entity.ServiceEntity;
import therooster.booking.enums.TypeService;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    @Named("stringToTypeService")
    static TypeService stringToTypeService(String value) {
        if (value == null) return null;
        return TypeService.valueOf(value.toUpperCase());
    }

    @Named("typeServiceToString")
    static String typeServiceToString(TypeService type) {
        return type == null ? null : type.name();
    }

    // CreateServiceDTO -> ServiceEntity
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "description", source = "description")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "price", source = "price")
    // l'id est généré par la BDD
    ServiceEntity toEntity(CreateServiceDTO dto);

    // ServiceEntity -> LireServiceDTO
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "price", source = "price")
    LireServiceDTO toDto(ServiceEntity entity);

    // Pour mapper des listes si besoin
    List<LireServiceDTO> toDtoList(List<ServiceEntity> entities);
}

