package therooster.booking.config.geoconding;


import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import com.opencagedata.jopencage.model.JOpenCageReverseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import therooster.booking.dto.request.GeocodingReverseRequestDTO;


@Service
@RequiredArgsConstructor
public class GeoCodingService {
    private final JOpenCageGeocoder jOpenCageGeocoder;


    public String reverseGeocode(GeocodingReverseRequestDTO dto) {
        JOpenCageReverseRequest reverseRequest = new JOpenCageReverseRequest(dto.getLatitude(), dto.getLongitude());
        reverseRequest.setLanguage(dto.getLanguage() != null ? dto.getLanguage() : "fr");
        reverseRequest.setLimit(1);
        reverseRequest.setNoAnnotations(true);

        JOpenCageResponse reverseResponse = jOpenCageGeocoder.reverse(reverseRequest);

        if (reverseResponse.getResults().isEmpty()) {
            throw new RuntimeException("No address found for coordinates " + dto.getLatitude() + ", " + dto.getLongitude());
        }
        return reverseResponse.getResults().get(0).getFormatted();
    }

    public JOpenCageLatLng forwardGeocode(String formatedAddresse, String countryCode) {
        JOpenCageForwardRequest forwardRequest = new JOpenCageForwardRequest(formatedAddresse);
        if (countryCode != null) {
            forwardRequest.setRestrictToCountryCode(countryCode);
        }

        JOpenCageResponse response = jOpenCageGeocoder.forward(forwardRequest);
        return response.getFirstPosition();
    }
}
