package eu.carfax.odometeranalysisservice;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.carfax.odometeranalysisservice.api.VehicleHistoryResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */
public class AbstractTestProvider {

    private final ObjectMapper objectMapper;

    protected AbstractTestProvider() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
    }

    protected VehicleHistoryResponse readVehicleHistoryFromFile(String filePath) {
        return readFromFile(VehicleHistoryResponse.class, filePath);
    }

    private <T> T readFromFile(Class<T> clazz, String resourceName) {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
            return objectMapper.readValue(stream, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getContentFromFile(String resourceName) throws IOException {
        return IOUtils.toString(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)), StandardCharsets.UTF_8.name());
    }
}
