package eu.carfax.odometeranalysisservice.external;

import eu.carfax.odometeranalysisservice.AbstractTestProvider;
import eu.carfax.odometeranalysisservice.api.VehicleHistoryRecord;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class VehicleHistoryGatewayIntegrationTest extends AbstractTestProvider {

    private static final String VIN = "VSSZZZ6JZ9R056308";
    @Autowired
    private VehicleHistoryGateway vehicleHistoryGateway;

    @Test
    public void testVehicleHistoryGateway() {
        List<VehicleHistoryRecord> expectedVehicleHistoryRecords = readVehicleHistoryFromFile("testData/testVehicleHistoryWithRollback.json").getRecords();
        List<VehicleHistoryRecord> vehicleHistoryRecords = vehicleHistoryGateway.getVehicleHistoryByVin(VIN);
        Assert.assertThat(vehicleHistoryRecords, IsIterableContainingInOrder.contains(expectedVehicleHistoryRecords.toArray()));
    }
}
