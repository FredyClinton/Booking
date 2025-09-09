package therooster.booking.entity;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("EMPLOYEE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee extends UserEntity {

    @Column(name = "score")
    private Integer royaltyPoints;

    @ManyToMany(mappedBy = "employees")
    private Set<Booking> assignedBookings = new HashSet<>();
}
