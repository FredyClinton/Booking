package therooster.booking.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import therooster.booking.enums.TypeDeRole;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TypeDeRole libelle;
}