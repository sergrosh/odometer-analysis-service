package eu.carfax.odometeranalysisservice.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */
@Configuration
@EnableConfigurationProperties(UserDatabase.class)
@ConfigurationProperties(prefix = "username-database")
class UserDatabase {

    @Getter
    @Setter
    private Map<String, StaticUser> users;

    StaticUser getUser(String key) {
        if (users == null) {
            throw new IllegalArgumentException("Missing configuration for username-database:users");
        }
        if (!users.containsKey(key)) {
            throw new IllegalArgumentException("Invalid user '" + key + "'");
        }
        return users.get(key);
    }

    @Getter
    @Setter
    @ToString
    static class StaticUser {
        private String username;
        private String password;
    }
}
