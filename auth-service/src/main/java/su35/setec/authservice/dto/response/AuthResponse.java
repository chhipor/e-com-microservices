package su35.setec.authservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {

    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpiresInSeconds;
    private long refreshTokenExpiresInSeconds;
    private UserResponse user;
}
