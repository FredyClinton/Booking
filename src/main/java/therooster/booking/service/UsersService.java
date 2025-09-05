package therooster.booking.service;

import therooster.booking.dto.response.LireUserDTO;
import therooster.booking.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface UsersService {
    void inscription(UserEntity user);

    void activation(Map<String, String> activation);

    void modifierMotDePasse(Map<String, String> params);

    void nouveauMotDePasse(Map<String, String> params);

    List<UserEntity> getAllUsers();

    List<LireUserDTO> getAllUsersWithoutPassword();
}
