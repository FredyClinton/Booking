package therooster.booking.dto.request;

import java.time.Instant;

public class CreateBookingDTO {

    private Instant appointmentDate;
    private String clientNote;
    private String internalNote;
    private Double price;

}
