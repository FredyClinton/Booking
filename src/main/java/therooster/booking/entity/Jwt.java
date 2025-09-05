package therooster.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "jwt")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Jwt extends BaseIdUuid {


    private boolean desactive;
    private boolean expire;
    private String value;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private UserEntity utilisateur;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private RefreshToken refreshToken;


}
