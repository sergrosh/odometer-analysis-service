package eu.carfax.odometeranalysisservice.web;

import eu.carfax.odometeranalysisservice.AbstractTestProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(username = "api", roles = "API")
@ActiveProfiles(profiles = "test")
public class OdometerAnalysisControllerIntegrationTest extends AbstractTestProvider {
    private static final String ROLLBACK_URI = "/odometer/rollback/";
    private static final String SUCCESS_VIN = "VSSZZZ6JZ9R056308";
    private static final String FAILURE_VIN = "VSSZZZ6JZ9R0563081";
    private static final String VEHICLE_HISTORY_OUTPUT_JSON = "testData/testVehicleHistoryWithRollback_out.json";
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void testSuccessResponseFromOdometerAnalysisController() throws Exception {
        mockMvc.perform(get(ROLLBACK_URI + SUCCESS_VIN)).andExpect(status().isOk());
    }

    @Test
    public void testFailureResponseFromOdometerAnalysisController() throws Exception {
        mockMvc.perform(get(ROLLBACK_URI + FAILURE_VIN)).andExpect(status().isBadRequest());
    }

    @Test
    public void testResponseBodyFromOdometerAnalysisController() throws Exception {
        MvcResult result = mockMvc.perform(get(ROLLBACK_URI + SUCCESS_VIN)).andReturn();
        String expectedContent = getContentFromFile(VEHICLE_HISTORY_OUTPUT_JSON);
        String content = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedContent, content, false);
    }
}
