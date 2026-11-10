package su35.setec.operationservice.security;

import java.util.List;
import java.util.UUID;

/**
 * Principal built from a validated auth-service access token.
 * {@code firstName}/{@code lastName} may be null for tokens issued before those claims existed.
 */
public record AuthenticatedUser(UUID userId, String email, String firstName, String lastName, List<String> roles) {
}
