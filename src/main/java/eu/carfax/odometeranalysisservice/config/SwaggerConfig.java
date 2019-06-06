package eu.carfax.odometeranalysisservice.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration for Springfox implementation of the Swagger 2 specification
 *
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */
@Configuration
@EnableSwagger2
@ConditionalOnWebApplication
public class SwaggerConfig {
    private static final Logger LOG = LoggerFactory.getLogger(SwaggerConfig.class);

    /**
     * The value has to be supplied as configuration parameter -Dswagger-ui.hostname=<servicename>-<env>
     */
    private final String hostname;

    public SwaggerConfig(@Value("${swagger-ui.hostname:}") final String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the docket
     */
    @Bean
    public Docket odometerApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("odometer-analysis-service")
                .apiInfo(apiInfo());
        if (StringUtils.isNoneBlank(hostname)) {
            docket = docket.host(hostname);
            LOG.info("Swagger ux will use '{}' as hostname for API 'try it' calls.", hostname);
        } else {
            LOG.info("Swagger ux will use default hostname for API 'try it' calls.");
        }
        return docket.select()
                .paths(PathSelectors.ant("/api/v1/odometer/**"))
                .build();
    }


    private ApiInfo apiInfo() {
        final Contact contact = new Contact("Sergii R", "www.carfax.eu", "some.email@email.com");
        return new ApiInfoBuilder()
                .title("Odometer Analysis Service API")
                .description("Service for calculation odometer rollback appearance")
                .contact(contact)
                .version("1.0")
                .build();
    }
}
