package therooster.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import therooster.booking.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface UsersRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u")
    Stream<UserEntity> findAllUsers();

}
