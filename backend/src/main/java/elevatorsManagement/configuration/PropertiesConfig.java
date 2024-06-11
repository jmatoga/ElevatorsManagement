package elevatorsManagement.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@Scope("singleton")
@PropertySource("classpath:paths.properties")
@Getter
public class PropertiesConfig {
    @SuppressWarnings("squid:S116")
    @Value("${elevatorsManagement.app.pagination.defaultSizeOfPage}")
    private int PAGE_SIZE;

    @SuppressWarnings("squid:S116")
    @Value("${elevatorsManagement.app.accessTokenCookieName}")
    private String accessTokenCookieName;

    @SuppressWarnings("squid:S116")
    @Value("${elevatorsManagement.app.refreshTokenCookieName}")
    private String refreshTokenCookieName;

    @SuppressWarnings("squid:S116")
    @Value("${elevatorsManagement.app.jwtExpirationMs}")
    private Long jwtExpirationMs;

    @SuppressWarnings("squid:S116")
    @Value("${elevatorsManagement.app.refreshTokenExpirationSec}")
    private Long refreshTokenExpirationSec;

    @Value("${elevatorsManagement.app.paths.login}")
    private String PATH_LOGIN;

    @Value("${elevatorsManagement.app.paths.logout}")
    private String PATH_LOGOUT;
}
