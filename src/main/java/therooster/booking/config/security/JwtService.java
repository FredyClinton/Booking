package therooster.booking.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import therooster.booking.entity.Jwt;
import therooster.booking.entity.RefreshToken;
import therooster.booking.entity.UserEntity;
import therooster.booking.exception.TokenNotFoundException;
import therooster.booking.repository.JwtRepository;
import therooster.booking.service.Impl.UsersServiceImpl;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class JwtService {
    private static final String REFRESH = "refresh";
    private static final String BEARER = "Bearer ";


    private final UsersServiceImpl utilisateurService;
    private final JwtRepository jwtRepository;

    @Value("${app.secret-key}")
    private String encryptioKey;

    public Map<String, String> generate(String username) {
        UserEntity utilisateur = this.utilisateurService.loadUserByUsername(username);

        // desactiver tous les token de l'utilisateur qui sont valide
        this.disableTokens(utilisateur);
        Map<String, String> generateJWT = new java.util.HashMap<>(this.generateJWT(utilisateur));

        RefreshToken refreshToken = RefreshToken
                .builder()
                .value(UUID.randomUUID().toString())
                .utilisateur(utilisateur)
                .expire(false)
                .creation(Instant.now())
                .expiration(Instant.now().plusMillis(30 * 60 * 1000))
                .build();


        final Jwt jwt = Jwt.builder()
                .desactive(false)
                .expire(false)
                .value(generateJWT.get(BEARER))
                .utilisateur(utilisateur)
                .refreshToken(refreshToken)
                .build();

        this.jwtRepository.save(jwt);
        generateJWT.put(REFRESH, refreshToken.getValue());
        return generateJWT;
    }

    private void disableTokens(UserEntity user) {
        final List<Jwt> jwtList =
                this.jwtRepository.findUtilisateurEmail(user.getEmail()
                ).peek(
                        jwt -> {
                            jwt.setDesactive(true);
                            jwt.setExpire(true);
                        }
                ).collect(Collectors.toList());
        this.jwtRepository.saveAll(jwtList);
    }

    private Map<String, String> generateJWT(UserEntity utilisateur) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + TimeUnit.MINUTES.toMillis(30);


        Map<String, Object> claims = Map.of(
                "firstname", utilisateur.getFirstName(),
                "email", utilisateur.getEmail(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, utilisateur.getEmail()
        );


        var bearer = Jwts.builder()
                .issuedAt(new Date(currentTime))
                .expiration(new Date(expirationTime))
                .subject(utilisateur.getEmail())
                .claims(claims)
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();

        return Map.of(BEARER, bearer);
    }

    private SecretKey getKey() {
        final byte[] decoder = Decoders.BASE64.decode(encryptioKey);
        return Keys.hmacShaKeyFor(decoder);
    }

    public void deconnexion() {
        UserEntity utilisateur = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.findUtilisateurValidToken(
                utilisateur.getEmail(),
                false,
                false
        ).orElseThrow(() -> new RuntimeException("Aucun token valide associe à ce user"));

        jwt.setExpire(true);
        jwt.setDesactive(true);

        this.jwtRepository.save(jwt);
    }


    public String extractUsername(String token) {
        return getClaims(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {

        Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.getClaims(token, Claims::getExpiration);
    }

    //methode generique qui recupère toutes une claim souhaité
    private <T> T getClaims(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    // methode pour recupere toutes les claims du token
    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Jwt tokenByValue(String value) {
        return this.jwtRepository.findByValue(value)
                .orElseThrow(TokenNotFoundException::new);
    }

    @Scheduled(cron = "@daily")
    public void removeUseLessJwt() {
        log.info("Suppression des token invalides a {}", Instant.now());
        this.jwtRepository.deleteByExpireAndDesactive(true, true);
    }

    public Map<String, String> refreshToken(Map<String, String> refreshTokenParams) {
        log.info("UUID fourni {}", refreshTokenParams.get("refresh"));
        Jwt jwt = this.jwtRepository.findByRefreshToken(refreshTokenParams.get(REFRESH)).orElseThrow(
                () -> new TokenNotFoundException("refresh token not found")
        );
        if (jwt.isExpire()
                || jwt.getRefreshToken().getExpiration().isBefore(Instant.now())) {
            throw new JwtException("refresh token expired");
        }
        this.disableTokens(jwt.getUtilisateur());
        return this.generate(jwt.getUtilisateur().getEmail());
    }
}