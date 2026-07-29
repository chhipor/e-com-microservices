package su35.setec.authservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.admin")
public record AdminProperties(
        String email,
        String password,
        String firstName,
        String lastName
) {
}
