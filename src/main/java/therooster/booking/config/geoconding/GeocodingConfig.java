package therooster.booking.config.geoconding;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeocodingConfig {

    @Value("${jopen.cage.geocoder.api.key}")
    private String apiKey;

    @Bean
    public JOpenCageGeocoder jOpenCageGeocoder() {
        return new JOpenCageGeocoder(apiKey);
    }
}
