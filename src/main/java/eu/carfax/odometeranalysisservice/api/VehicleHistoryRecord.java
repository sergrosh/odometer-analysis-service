package eu.carfax.odometeranalysisservice.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(notes = "Vehicle identification number", required = true)
    private String vin;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(notes = "Date of the history record")
    private Date date;
    @JsonProperty("data_provider_id")
    @ApiModelProperty(notes = "Id of the data provider")
    private Long dataProviderId;
    @JsonProperty("odometer_reading")
    @ApiModelProperty(notes = "Odometer value for this history record")
    private Integer odometerReading;
    @JsonProperty("service_details")
    @ApiModelProperty(notes = "Extra service details")
    private List<String> serviceDetails;
    @JsonProperty("odometer_rollback")
    @ApiModelProperty(notes = "Odometer rollback appearance (appeared only if odometer rollback took a place)")
    private Boolean odometerRollback;
    @JsonProperty("mileage_inconsistency")
    @ApiModelProperty(notes = "Mileage inconsistency")
    private Boolean mileageInconsistency;
}

