package therooster.booking.dto.request;

import java.util.Date;

public record CreateUserRequestDTO(String firstName,
                                   String lastName,
                                   String email,
                                   String password,
                                   String phone,
                                   Date birthDate) {
}
