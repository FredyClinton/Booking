package therooster.booking.service;

import therooster.booking.dto.request.CreateServiceDTO;
import therooster.booking.dto.response.LireServiceDTO;

import java.util.List;
import java.util.UUID;

public interface ServicesService {
    void deleteService(UUID id);

    LireServiceDTO createService(CreateServiceDTO createServiceDTO);

    List<LireServiceDTO> listServices();

    LireServiceDTO lireServices(UUID serviceId);
}
