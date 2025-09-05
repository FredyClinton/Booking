package therooster.booking.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh-token")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken extends BaseIdUuid {


    private boolean expire;
    private String value;

    private Instant creation;
    private Instant expiration;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private UserEntity utilisateur;


}