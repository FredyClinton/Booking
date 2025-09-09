package therooster.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import therooster.booking.enums.TypeService;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LireServiceDTO {
    private UUID id;
    private String description;
    private TypeService type;
}
