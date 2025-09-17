package therooster.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeocodingReverseRequestDTO {
    private Double latitude;
    private Double longitude;
    private String language;
}

