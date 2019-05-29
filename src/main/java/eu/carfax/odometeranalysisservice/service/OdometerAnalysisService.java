package eu.carfax.odometeranalysisservice.service;

import eu.carfax.odometeranalysisservice.api.VehicleHistoryRecord;

import java.util.List;

/**
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */
public interface OdometerAnalysisService {
    /**
     * @param vin vehicle identification number
     * @return the List of analyzed vehicle history records
     */
    List<VehicleHistoryRecord> getAnalyzedVehicleRecordsByVin(final String vin);
}
