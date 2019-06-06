package eu.carfax.odometeranalysisservice.controller;

import eu.carfax.odometeranalysisservice.api.VehicleHistoryRecord;
import eu.carfax.odometeranalysisservice.api.VehicleHistoryResponse;
import eu.carfax.odometeranalysisservice.service.OdometerAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for receiving analyzed vehicle history with odometer rollback info
 *
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */

@RestController
@Api(value = "Odometer analysis", tags = "Odometer Rollback", produces = "application/json")
@RequestMapping("/api/v1/odometer/")
public class OdometerAnalysisController {

    private final OdometerAnalysisService odometerAnalysisService;

    @Autowired
    public OdometerAnalysisController(final OdometerAnalysisService odometerAnalysisService) {
        this.odometerAnalysisService = odometerAnalysisService;
    }

    @ApiOperation(value = "Get vehicle history with analyzed odometer rollback info")
    @GetMapping(value = "/rollback/{vin}", produces = "application/json; charset=UTF-8")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Calculated successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource")
    })
    public ResponseEntity<VehicleHistoryResponse> getVehicleHistoryWithOdometerRollbackInfo(@ApiParam(value = "Vehicle identification number", required = true) @PathVariable(value = "vin") String vin) {
        VehicleHistoryResponse vehicleHistoryResponse = new VehicleHistoryResponse();
        List<VehicleHistoryRecord> records = odometerAnalysisService.getAnalyzedVehicleRecordsByVin(vin);
        vehicleHistoryResponse.setRecords(records);
        if (CollectionUtils.isNotEmpty(records)) {
            return ResponseEntity.ok(vehicleHistoryResponse);
        } else {
            vehicleHistoryResponse.setReason("Provided vin is not correct or vehicle history was not found");
            return ResponseEntity.badRequest().body(vehicleHistoryResponse);
        }
    }
}
