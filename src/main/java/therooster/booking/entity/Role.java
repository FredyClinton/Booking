package therooster.booking.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import therooster.booking.enums.TypeDeRole;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Role extends BaseIdUuid {


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TypeDeRole libelle;
}