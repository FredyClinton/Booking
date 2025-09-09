package therooster.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import therooster.booking.enums.TypeService;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateServiceDTO {
    private String description;
    private TypeService type;
}
