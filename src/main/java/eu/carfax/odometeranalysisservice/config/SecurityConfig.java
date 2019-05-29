package eu.carfax.odometeranalysisservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Simple spring security config
 *
 * @author Sergii Roshchupkin
 * @since 2019-05-28.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String INTERNAL_ROLE = "INTERNAL";
    private static final String API_ROLE = "API";
    private final UserDatabase userDatabase;

    @Autowired
    public SecurityConfig(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        if (userDatabase == null) {
            throw new IllegalStateException("Missing injected bean 'userDatabase'");
        }
        UserDatabase.StaticUser apiUser = userDatabase.getUser("api");
        UserDatabase.StaticUser monitoringUser = userDatabase.getUser("monitor");

        builder.inMemoryAuthentication()
                .withUser(monitoringUser.getUsername()).password(monitoringUser.getPassword()).roles(INTERNAL_ROLE)
                .and()
                .withUser(apiUser.getUsername()).password(apiUser.getPassword()).roles(API_ROLE);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/odometer/**").hasRole(API_ROLE)
                .antMatchers("/internal/**").hasRole(INTERNAL_ROLE)
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .httpBasic();
    }
}
