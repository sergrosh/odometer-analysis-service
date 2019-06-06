package eu.carfax.odometeranalysisservice.service;

import eu.carfax.odometeranalysisservice.AbstractTestProvider;
import eu.carfax.odometeranalysisservice.api.VehicleHistoryRecord;
import eu.carfax.odometeranalysisservice.external.VehicleHistoryGateway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */
public class OdometerAnalyzeServiceTest extends AbstractTestProvider {

    private static final String ONE_ROLLBACK_VIN = "VSSZZZ6JZ9R056308";
    private static final String NO_ROLLBACK_VIN = "VSSZZZ6JZ9R056307";
    private static final String MULTIPLE_ROLLBACKS_VIN = "VSSZZZ6JZ9R056309";
    private static final String NO_DATE_ROLLBACK_VIN = "VSSZZZ6JZ9R056310";
    private static final String MULTIPLE_ROLLBACKS_AND_UNSORTED_VIN = "VSSZZZ6JZ9R056309";
    private OdometerAnalysisService odometerAnalysisService;
    @Mock
    private VehicleHistoryGateway vehicleHistoryGateway;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(vehicleHistoryGateway.getVehicleHistoryByVin(ONE_ROLLBACK_VIN))
                .willReturn(readVehicleHistoryFromFile("testData/testVehicleHistoryWithRollback.json").getRecords());

        given(vehicleHistoryGateway.getVehicleHistoryByVin(NO_ROLLBACK_VIN))
                .willReturn(readVehicleHistoryFromFile("testData/testVehicleHistoryWithoutRollback.json").getRecords());

        given(vehicleHistoryGateway.getVehicleHistoryByVin(MULTIPLE_ROLLBACKS_VIN))
                .willReturn(readVehicleHistoryFromFile("testData/testVehicleHistoryWithMultipleRollbacks.json").getRecords());

        given(vehicleHistoryGateway.getVehicleHistoryByVin(NO_DATE_ROLLBACK_VIN))
                .willReturn(readVehicleHistoryFromFile("testData/testVehicleHistoryWithoutDate.json").getRecords());

        given(vehicleHistoryGateway.getVehicleHistoryByVin(MULTIPLE_ROLLBACKS_VIN))
                .willReturn(readVehicleHistoryFromFile("testData/testVehicleHistoryWithMultipleRollbacks.json").getRecords());


        odometerAnalysisService = new OdometerAnalysisServiceImpl(vehicleHistoryGateway);
    }

    @Test
    public void testOneMileageInconsistencyCount() {
        List<VehicleHistoryRecord> records = odometerAnalysisService.getAnalyzedVehicleRecordsByVin(ONE_ROLLBACK_VIN);
        Assert.assertEquals(1L, records.stream().filter(record -> record.getMileageInconsistency() != null && record.getMileageInconsistency()).count());
    }

    @Test
    public void testOneRollbackPositionMileageInconsistency() {
        Assert.assertTrue(odometerAnalysisService.getAnalyzedVehicleRecordsByVin(ONE_ROLLBACK_VIN).get(3).getMileageInconsistency());
    }

    @Test
    public void testWithoutRollbackCount() {
        List<VehicleHistoryRecord> records = odometerAnalysisService.getAnalyzedVehicleRecordsByVin(NO_ROLLBACK_VIN);
        Assert.assertEquals(0L, records.stream().filter(record -> record.getOdometerRollback() != null).count());
    }

    @Test
    public void testWithMultipleRollbacksCount() {
        List<VehicleHistoryRecord> records = odometerAnalysisService.getAnalyzedVehicleRecordsByVin(MULTIPLE_ROLLBACKS_VIN);
        Assert.assertEquals(2L, records.stream().filter(record -> record.getOdometerRollback() != null).count());
    }

    @Test
    public void testWithMultipleRollbacksPosition1() {
        Assert.assertTrue(odometerAnalysisService.getAnalyzedVehicleRecordsByVin(MULTIPLE_ROLLBACKS_VIN).get(2).getOdometerRollback());
    }

    @Test
    public void testWithMultipleRollbacksPosition2() {
        Assert.assertNull(odometerAnalysisService.getAnalyzedVehicleRecordsByVin(MULTIPLE_ROLLBACKS_VIN).get(3).getOdometerRollback());
    }

    @Test
    public void testWithMultipleRollbacksPosition3() {
        Assert.assertTrue(odometerAnalysisService.getAnalyzedVehicleRecordsByVin(MULTIPLE_ROLLBACKS_VIN).get(4).getOdometerRollback());
    }

    @Test
    public void testWithNoDateMileageInconsistencyCount() {
        List<VehicleHistoryRecord> records = odometerAnalysisService.getAnalyzedVehicleRecordsByVin(NO_DATE_ROLLBACK_VIN);
        Assert.assertEquals(1L, records.stream().filter(record -> record.getMileageInconsistency() != null).count());
    }

    @Test
    public void testWithNoDateMileageInconsistencyPosition1() {
        Assert.assertTrue(odometerAnalysisService.getAnalyzedVehicleRecordsByVin(NO_DATE_ROLLBACK_VIN).get(3).getMileageInconsistency());
    }

    @Test
    public void testWithNoDateRollbackOdometerReading1() {
        Assert.assertEquals(Integer.valueOf(6400), odometerAnalysisService.getAnalyzedVehicleRecordsByVin(NO_DATE_ROLLBACK_VIN).get(3).getOdometerReading());
    }

    @Test
    public void testWithNoDateRollbackPosition2() {
        Assert.assertNull(odometerAnalysisService.getAnalyzedVehicleRecordsByVin(NO_DATE_ROLLBACK_VIN).get(4).getOdometerRollback());
    }

    @Test
    public void testWithNoDateRollbackOdometerReading2() {
        Assert.assertEquals(Integer.valueOf(5600), odometerAnalysisService.getAnalyzedVehicleRecordsByVin(NO_DATE_ROLLBACK_VIN).get(4).getOdometerReading());
    }

    @Test
    public void testWithMultipleRollbacksAndUnsortedPosition1() {
        Assert.assertTrue(odometerAnalysisService.getAnalyzedVehicleRecordsByVin(MULTIPLE_ROLLBACKS_AND_UNSORTED_VIN).get(2).getOdometerRollback());
    }

    @Test
    public void testWithMultipleRollbacksAndUnsortedPosition2() {
        Assert.assertNull(odometerAnalysisService.getAnalyzedVehicleRecordsByVin(MULTIPLE_ROLLBACKS_AND_UNSORTED_VIN).get(3).getOdometerRollback());
    }

    @Test
    public void testWithMultipleRollbacksAndUnsortedPosition3() {
        Assert.assertTrue(odometerAnalysisService.getAnalyzedVehicleRecordsByVin(MULTIPLE_ROLLBACKS_AND_UNSORTED_VIN).get(4).getOdometerRollback());
    }
}
