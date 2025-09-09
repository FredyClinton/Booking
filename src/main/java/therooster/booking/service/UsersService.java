package therooster.booking.service;

import therooster.booking.dto.request.ChangePasswordDTO;
import therooster.booking.dto.request.CreateUserRequestDTO;
import therooster.booking.dto.response.LireUserDTO;
import therooster.booking.entity.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UsersService {

    void inscription(CreateUserRequestDTO utilisateur);

    void activeAccount(Map<String, String> activation);

    void sendCodeToChangePasseword(Map<String, String> params);

    void changePassword(ChangePasswordDTO changePasswordDTO);

    List<UserEntity> getAllUsers();

    List<LireUserDTO> getAllUsersWithoutPassword();

    void deleteUser(UUID id, String currentUserUsername);
}
