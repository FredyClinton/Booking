package therooster.booking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import therooster.booking.dto.request.ChangePasswordDTO;
import therooster.booking.dto.request.CreateUserRequestDTO;
import therooster.booking.dto.response.LireUserDTO;
import therooster.booking.entity.Role;
import therooster.booking.entity.UserEntity;
import therooster.booking.entity.Validation;
import therooster.booking.enums.TypeDeRole;
import therooster.booking.mapper.UserEntityMapper;
import therooster.booking.repository.RoleRepository;
import therooster.booking.repository.UsersRepository;
import therooster.booking.service.UsersService;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UserDetailsService, UsersService {
    private final UsersRepository usersRepository;
    private final ValidationServiceImpl validationService;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityMapper userEntityMapper;
    private final RoleRepository roleRepository;


    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.usersRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
    }


    @Override
    public void inscription(CreateUserRequestDTO dtoToCreatUser) {

        String email = dtoToCreatUser.getEmail();
        if (email == null || email.isBlank() || !email.contains("@") || !email.contains(".")) {
            throw new RuntimeException("Email invalid");
        }
        String rawPassword = dtoToCreatUser.getPassword();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new RuntimeException("Password invalid");
        }
        UserEntity user = this.userEntityMapper.toClientEntity(dtoToCreatUser);
        String cryptPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(cryptPassword);

        boolean userExist = this.usersRepository.existsByEmail(dtoToCreatUser.getEmail());

        if (userExist) {
            throw new RuntimeException("This Email already exists");
        }

        Role role = roleRepository.findByLibelle(TypeDeRole.CLIENT)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setLibelle(TypeDeRole.CLIENT);
                    return roleRepository.save(newRole); // Sauvegarde explicite
                });
        user.setRole(role);

        UserEntity saveUser = this.usersRepository.save(user);
        this.validationService.enregistrer(saveUser);
    }

    @Override
    public void activeAccount(Map<String, String> activation) {
        Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code"));
        if (!Instant.now().isBefore(validation.getExpiration())) {
            throw new RuntimeException("Activation code expired");
        }
        UserEntity userToActive = this.usersRepository.findById(validation.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userToActive.setActif(true);
        this.usersRepository.save(userToActive);

    }

    // TODO: Revoir cette implementation
    @Override
    public void sendCodeToChangePasseword(Map<String, String> params) {
        UserEntity utilisateur = this.loadUserByUsername(params.get("email"));
        this.validationService.enregistrer(utilisateur);

    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        UserEntity utilisateur = this.loadUserByUsername(changePasswordDTO.email());
        Validation validation = this.validationService.lireEnFonctionDuCode(changePasswordDTO.code());
        if (validation.getUser().getEmail().equals(utilisateur.getEmail())) {
            String cryptPassword = this.passwordEncoder.encode(changePasswordDTO.newPassword());
            utilisateur.setPassword(cryptPassword);
            this.usersRepository.save(utilisateur);
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return this.usersRepository.findAll();
    }

    @Override
    public List<LireUserDTO> getAllUsersWithoutPassword() {

        try (Stream<UserEntity> utilisateurStream = this.usersRepository.findAllUsers()) {

            return utilisateurStream
                    .map(this.userEntityMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private boolean hasRole(UserEntity user, String role) {
        return user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    @Override
    public void deleteUser(UUID id, String currentUserUsername) {
        UserEntity userForId = this.usersRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        UserEntity userForUsername = this.usersRepository.findByEmail(currentUserUsername).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        // TODO: Ameliorer cette implémentation
        if (userForId.equals(userForUsername) || hasRole(userForUsername, "ROLE_ADMIN")) {
            this.usersRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not authorized");
        }


    }


}
