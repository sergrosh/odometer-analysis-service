package eu.carfax.odometeranalysisservice.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Config of Rest template for connection to coding-challenge.carfax.eu
 * credentials provider can be added if we need some auth to connect
 *
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */

@Configuration
public class VehicleHistoryGatewayConfig {

    private final Integer readTimeout;
    private final Integer connectTimeout;

    public VehicleHistoryGatewayConfig(
            @Value("${rest.vehicle-history.read.timeout}") final Integer readTimeout,
            @Value("${rest.vehicle-history.read.connect}") final Integer connectTimeout) {
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
    }

    @Bean(name = "vehicleHistoryRestTemplate")
    public RestTemplate vehicleHistoryRestTemplate() {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        return new RestTemplate(clientHttpRequestFactory(httpClient));
    }

    private ClientHttpRequestFactory clientHttpRequestFactory(CloseableHttpClient httpClient) {
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(readTimeout);
        factory.setConnectTimeout(connectTimeout);
        return factory;
    }
}
