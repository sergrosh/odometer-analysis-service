package eu.carfax.odometeranalysisservice.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleHistoryResponse {
    @JsonProperty("records")
    private List<VehicleHistoryRecord> records;
    private String reason;
}
