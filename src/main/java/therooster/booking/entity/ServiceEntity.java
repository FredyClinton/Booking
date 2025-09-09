package therooster.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import therooster.booking.enums.TypeService;

@Entity
@Table(name = "services")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceEntity extends BaseIdUuid {
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeService type;

}
