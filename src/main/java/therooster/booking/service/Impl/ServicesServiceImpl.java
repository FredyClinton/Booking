package therooster.booking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import therooster.booking.dto.request.CreateServiceDTO;
import therooster.booking.dto.response.LireServiceDTO;
import therooster.booking.entity.ServiceEntity;
import therooster.booking.mapper.ServiceMapper;
import therooster.booking.repository.ServiceRepository;
import therooster.booking.service.ServicesService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Override
    public void deleteService(UUID id) {
        if (!serviceRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found");
        }
        serviceRepository.deleteById(id);
    }

    @Override
    public LireServiceDTO createService(CreateServiceDTO createServiceDTO) {
        ServiceEntity entity = serviceMapper.toEntity(createServiceDTO);
        ServiceEntity saved = serviceRepository.save(entity);
        return serviceMapper.toDto(saved);
    }

    public List<LireServiceDTO> listServices() {
        List<ServiceEntity> entities = serviceRepository.findAll();
        return serviceMapper.toDtoList(entities);
    }

    @Override
    public LireServiceDTO lireServices(UUID serviceId) {
        ServiceEntity serviceEntity = serviceRepository.findById(serviceId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found")
        );
        return serviceMapper.toDto(serviceEntity);
    }
}
