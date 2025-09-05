package therooster.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import therooster.booking.entity.Role;
import therooster.booking.enums.TypeDeRole;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByLibelle(TypeDeRole libelle);
}
