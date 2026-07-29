package su35.setec.cronservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import su35.setec.cronservice.config.JwtProperties;
import su35.setec.cronservice.util.JwtTokenType;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public JwtTokenType extractTokenType(String token) {
        return JwtTokenType.valueOf(extractClaims(token).get("tokenType", String.class));
    }

    public boolean isAccessTokenValid(String token, String username) {
        Claims claims = extractClaims(token);
        return username.equals(claims.getSubject())
                && extractTokenType(token) == JwtTokenType.ACCESS
                && claims.getExpiration().after(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
    }
}
