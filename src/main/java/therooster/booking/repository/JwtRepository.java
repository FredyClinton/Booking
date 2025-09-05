package therooster.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import therooster.booking.entity.Jwt;
import therooster.booking.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface JwtRepository extends JpaRepository<Jwt, UUID> {

    Optional<Jwt> findByValue(String value);

    Optional<Jwt> findByUtilisateurAndDesactiveAndExpire(UserEntity utilisateur, boolean desactive, boolean expire);

    @Query("FROM Jwt j where j.expire =false  AND j.desactive = false AND j.utilisateur.email =:email ")
    Optional<Jwt> findUtilisateurValidToken(String email);

    @Query("FROM Jwt j where j.expire =:expire  AND j.desactive =:desactive AND j.utilisateur.email =:email ")
    Optional<Jwt> findUtilisateurValidToken(String email, boolean desactive, boolean expire);

    @Query("FROM Jwt j where  j.utilisateur.email = :email ")
    Stream<Jwt> findUtilisateurEmail(String email);

    void deleteByExpireAndDesactive(boolean expire, boolean desactive);


    @Query("FROM Jwt j where  j.refreshToken.value = :value ")
    Optional<Jwt> findByRefreshToken(String value);
}
