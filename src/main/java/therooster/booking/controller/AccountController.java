package therooster.booking.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import therooster.booking.config.security.JwtService;
import therooster.booking.dto.request.AuthenticationDTO;
import therooster.booking.dto.request.ChangePasswordDTO;
import therooster.booking.dto.request.CreateUserRequestDTO;
import therooster.booking.dto.response.LireUserDTO;
import therooster.booking.mapper.UserEntityMapper;
import therooster.booking.service.UsersService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/accounts", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {

    private final AuthenticationManager authenticationManager;
    private final UsersService utilisateurService;
    private final JwtService jwtService;
    private final UserEntityMapper userEntityMapper;


    @PostMapping(path = "/inscription")
    public ResponseEntity<Void> inscription(@RequestBody CreateUserRequestDTO dto) {


        this.utilisateurService.inscription(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/activation")
    public ResponseEntity<String> activation(@RequestBody Map<String, String> activation) {

        this.utilisateurService.activeAccount(activation);
        return ResponseEntity.ok("Compte activé");
    }

    @PostMapping(path = "/connexion")
    public ResponseEntity<Map<String, String>> connexion(@RequestBody AuthenticationDTO dto) {
        final Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dto.username(),
                                dto.password()
                        )
                );
        log.info("result {}", authenticate.isAuthenticated());
        if (authenticate.isAuthenticated()) {
            var token = jwtService.generate(dto.username());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/deconnexion")
    public ResponseEntity<Void> deconnexion() {
        this.jwtService.deconnexion();
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/change-password")
    public ResponseEntity<Void> modifierMotDePasse(@RequestBody Map<String, String> email) {
        // TODO: implement methode
        this.utilisateurService.sendCodeToChangePasseword(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/new-password")
    public ResponseEntity<Void> newPassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        // TODO: implement methode
        this.utilisateurService.changePassword(changePasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/refresh-token")
    public @ResponseBody Map<String, String> refreshToken(@RequestBody Map<String, String> params) {
        // TODO: implement methode
        return this.jwtService.refreshToken(params);
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<LireUserDTO>> getsAccounts() {
        List<LireUserDTO> users = this.utilisateurService.getAllUsersWithoutPassword();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID accountId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserUsername = authentication.getName();
        this.utilisateurService.deleteUser(accountId, currentUserUsername);
        return ResponseEntity.noContent().build();

    }
}
