package therooster.booking.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
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

    @OneToMany(mappedBy = "employee")
    private List<Schedule> schedules;
}
