package eu.carfax.odometeranalysisservice.external;

import eu.carfax.odometeranalysisservice.api.VehicleHistoryRecord;
import eu.carfax.odometeranalysisservice.api.VehicleHistoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */

@Component
@Slf4j
public class VehicleHistoryGateway {

    private final RestTemplate vehicleHistoryRestTemplate;
    private final String vehicleHistoryServiceUrl;
    private final String getByVinUri;

    @Autowired
    public VehicleHistoryGateway(final RestTemplate vehicleHistoryRestTemplate,
                                 @Value("${rest.vehicle-history.url}") final String vehicleHistoryServiceUrl,
                                 @Value("${rest.vehicle-history.get-by-vin-uri}") final String getByVinUri) {
        this.vehicleHistoryRestTemplate = vehicleHistoryRestTemplate;
        this.vehicleHistoryServiceUrl = vehicleHistoryServiceUrl;
        this.getByVinUri = getByVinUri;
    }

    /**
     * connect to coding-challenge rest api and fetch vehicle history by vin
     *
     * @param vin vehicle identification number
     * @return the List of vehicle history records
     */
    public List<VehicleHistoryRecord> getVehicleHistoryByVin(String vin) {
        URI requestURI = UriComponentsBuilder.fromHttpUrl(vehicleHistoryServiceUrl + getByVinUri).build()
                .expand(vin)
                .encode().toUri();
        try {
            ResponseEntity<VehicleHistoryResponse> response = vehicleHistoryRestTemplate.getForEntity(requestURI, VehicleHistoryResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                VehicleHistoryResponse vehicleHistoryResponse = response.getBody();
                if (vehicleHistoryResponse != null && CollectionUtils.isNotEmpty(vehicleHistoryResponse.getRecords())) {
                    return vehicleHistoryResponse.getRecords();
                } else {
                    log.error("The vehicle history for requested vin: {} is not presented or response was not correctly parsed", vin);
                    return Collections.emptyList();
                }
            } else {
                log.error("Error while retrieving vehicle history by vin {}, status: {}", vin, response.getStatusCode().getReasonPhrase());
                return Collections.emptyList();
            }
        } catch (Exception ex) {
            log.error("Exception while retrieving vehicle history by vin {}, cause: {}", vin, ex.getMessage());
            return Collections.emptyList();
        }
    }
}


