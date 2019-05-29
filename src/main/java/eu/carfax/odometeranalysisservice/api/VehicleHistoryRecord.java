package eu.carfax.odometeranalysisservice.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleHistoryRecord {
    private String vin;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;
    @JsonProperty("data_provider_id")
    private Long dataProviderId;
    @JsonProperty("odometer_reading")
    private Integer odometerReading;
    @JsonProperty("service_details")
    private List<String> serviceDetails;
    @JsonProperty("odometer_rollback")
    private Boolean odometerRollback;
}
