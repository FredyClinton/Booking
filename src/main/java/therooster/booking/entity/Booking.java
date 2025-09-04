package therooster.booking.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import therooster.booking.enums.BookingStatus;

import java.time.Instant;

@Entity
@Table(name = "bookings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking extends BaseIdUuid {


    private Instant appointmentDate;
    private String clientNote;
    private String internalNote;
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.SUBMITTED;
    
}
