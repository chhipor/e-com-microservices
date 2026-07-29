package su35.setec.authservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import su35.setec.authservice.config.JwtProperties;
import su35.setec.authservice.entity.User;
import su35.setec.authservice.util.JwtTokenType;
import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties properties;

    public String generateAccessToken(User user) {
        return buildToken(user, JwtTokenType.ACCESS, Instant.now().plus(properties.accessTokenExpirationMinutes(), ChronoUnit.MINUTES));
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, JwtTokenType.REFRESH, Instant.now().plus(properties.refreshTokenExpirationDays(), ChronoUnit.DAYS));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(extractAllClaims(token).get("userId", String.class));
    }

    public JwtTokenType extractTokenType(String token) {
        return JwtTokenType.valueOf(extractAllClaims(token).get("tokenType", String.class));
    }

    public boolean isTokenValid(String token, String expectedUsername, JwtTokenType expectedType) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject().equals(expectedUsername)
                && extractTokenType(token) == expectedType
                && claims.getExpiration().after(new Date());
    }

    public long getAccessTokenExpirationSeconds() {
        return ChronoUnit.MINUTES.getDuration().multipliedBy(properties.accessTokenExpirationMinutes()).toSeconds();
    }

    public long getRefreshTokenExpirationSeconds() {
        return ChronoUnit.DAYS.getDuration().multipliedBy(properties.refreshTokenExpirationDays()).toSeconds();
    }

    private String buildToken(User user, JwtTokenType tokenType, Instant expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());
        claims.put("tokenType", tokenType.name());
        claims.put("roles", user.getRoles().stream().map(role -> role.getName()).toList());
        claims.put("issuer", properties.issuer());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
