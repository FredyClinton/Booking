package therooster.booking.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import therooster.booking.config.security.JwtService;
import therooster.booking.dto.request.AuthenticationDTO;
import therooster.booking.dto.request.CreateUserRequestDTO;
import therooster.booking.mapper.UserEntityMapper;
import therooster.booking.service.UsersService;

import java.net.URI;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {

    private final AuthenticationManager authenticationManager;
    private final UsersService utilisateurService;
    private final JwtService jwtService;
    private final UserEntityMapper userEntityMapper;


    @PostMapping(path = "/inscription")
    public ResponseEntity<Void> inscription(@RequestBody CreateUserRequestDTO dto) {


        this.utilisateurService.inscription(dto);
        return ResponseEntity.created(URI.create("Inscription réussie. Activez votre compte")).build();
    }

    @PostMapping(path = "/activation")
    public ResponseEntity<Void> activation(@RequestBody Map<String, String> activation) {

        this.utilisateurService.activation(activation);
        return ResponseEntity.noContent().build();
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
        this.utilisateurService.modifierMotDePasse(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/new-password")
    public ResponseEntity<Void> nouveauMotDePasse(@RequestBody Map<String, String> email) {
        // TODO: implement methode
        this.utilisateurService.nouveauMotDePasse(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/refresh-token")
    public @ResponseBody Map<String, String> refreshToken(@RequestBody Map<String, String> params) {
        // TODO: implement methode
        return this.jwtService.refreshToken(params);
    }
}
