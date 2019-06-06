package eu.carfax.odometeranalysisservice.service;

import eu.carfax.odometeranalysisservice.api.VehicleHistoryRecord;
import eu.carfax.odometeranalysisservice.external.VehicleHistoryGateway;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */

@Service
public class OdometerAnalysisServiceImpl implements OdometerAnalysisService {

    private final VehicleHistoryGateway vehicleHistoryGateway;

    @Autowired
    public OdometerAnalysisServiceImpl(VehicleHistoryGateway vehicleHistoryGateway) {
        this.vehicleHistoryGateway = vehicleHistoryGateway;
    }

    /**
     * Get vehicle history sorted by date (nulls last) with added the appropriate "odometer rollback" info (if it's needed)
     *
     * @param vin vehicle identification number
     * @return the List of sorted (by date, nulls last) vehicle history records with added (if it's needed) "odometer rollback" info
     */
    @Override
    public List<VehicleHistoryRecord> getAnalyzedVehicleRecordsByVin(final String vin) {
        List<VehicleHistoryRecord> vehicleHistoryRecords = vehicleHistoryGateway.getVehicleHistoryByVin(vin);
        if (CollectionUtils.isNotEmpty(vehicleHistoryRecords)) {
            List<VehicleHistoryRecord> sortedVehicleHistoryRecords = vehicleHistoryRecords.stream()
                    .sorted(Comparator.comparing(VehicleHistoryRecord::getDate, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());

            VehicleHistoryRecord previous;
            VehicleHistoryRecord current;
            ListIterator<VehicleHistoryRecord> listIterator = sortedVehicleHistoryRecords.listIterator();
            while (listIterator.hasNext()) {
                if (listIterator.hasPrevious()) {
                    previous = listIterator.previous();
                    listIterator.next();
                } else {
                    listIterator.next();
                    continue;
                }
                current = listIterator.next();
                if (current.getDate() == null) {
                    break;
                }

                if (previous.getOdometerReading() > current.getOdometerReading()) {
                    if (listIterator.hasNext()) {
                        if (previous.getOdometerReading() >= listIterator.next().getOdometerReading()) {
                            current.setMileageInconsistency(true);
                        } else {
                            current.setOdometerRollback(true);
                        }
                    } else {
                        current.setOdometerRollback(true);
                    }
                }
            }
            return sortedVehicleHistoryRecords;
        }
        return vehicleHistoryRecords;
    }
}
