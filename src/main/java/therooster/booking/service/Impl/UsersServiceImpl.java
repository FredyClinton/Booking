package therooster.booking.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
import java.util.Optional;
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
    public void inscription(CreateUserRequestDTO utilisateur) {

        System.out.println("inscription");
        String email = utilisateur.email();
        if (email == null || email.isBlank() || !email.contains("@") || !email.contains(".")) {
            throw new RuntimeException("Email invalid");
        }
        String rawPassword = utilisateur.password();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new RuntimeException("Password invalid");
        }
        UserEntity user = this.userEntityMapper.toClientEntity(utilisateur);
        String mdpCrypt = passwordEncoder.encode(rawPassword);
        System.out.println("fin du mapping");
        user.setPassword(mdpCrypt);

        Optional<UserEntity> optionalUser = usersRepository.findByEmail(utilisateur.email());

        if (optionalUser.isPresent()) {
            throw new RuntimeException("This email is already use");
        }

        Role role = roleRepository.findByLibelle(TypeDeRole.CLIENT)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    System.out.println("new role");
                    newRole.setLibelle(TypeDeRole.CLIENT);
                    System.out.println("save role");
                    return roleRepository.save(newRole); // Sauvegarde explicite
                });
        user.setRole(role);
        System.out.println("set role");
        var saveUser = this.usersRepository.save(user);
        System.out.println("saveUser ");
        this.validationService.enregistrer(saveUser);

    }

    @Override
    public void activation(Map<String, String> activation) {
        Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code"));
        if (!Instant.now().isBefore(validation.getExpiration())) {
            throw new RuntimeException("Activation code expired");
        }
        var utilisateurAActiver = this.usersRepository.findById(validation.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        utilisateurAActiver.setActif(true);
        this.usersRepository.save(utilisateurAActiver);

    }

    @Override
    public void modifierMotDePasse(Map<String, String> params) {
        UserEntity utilisateur = this.loadUserByUsername(params.get("email"));

        this.validationService.enregistrer(utilisateur);
        // TODO: Revoir cette implementation
    }

    @Override
    public void nouveauMotDePasse(Map<String, String> params) {

        UserEntity utilisateur = this.loadUserByUsername(params.get("email"));
        Validation validation = this.validationService.lireEnFonctionDuCode(params.get("code"));
        if (validation.getUser().getEmail().equals(utilisateur.getEmail())) {
            String mdpCrypt = this.passwordEncoder.encode(params.get("password"));
            utilisateur.setPassword(mdpCrypt);
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
}
