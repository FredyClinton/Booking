package therooster.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED) // on choisit JOINED pour éviter colonnes nulles
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@Table(name = "users")
public abstract class UserEntity extends BaseIdUuid {
    protected String firstName;
    protected String lastName;
    protected String password;
    protected String email;
    protected String phone;

    protected Date birthDate;
    @CreationTimestamp
    protected Date createdAt;
    @UpdateTimestamp
    protected Date updatedAt;

}
