package therooster.booking.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends UserEntity{
}
