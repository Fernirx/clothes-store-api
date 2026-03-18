package vn.fernirx.clothes.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.fernirx.clothes.common.constant.SecurityConstants;
import vn.fernirx.clothes.common.exception.TokenException;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(
                        jwtProperties.getSecret()
                )
        );
    }

    /* ================== PUBLIC PROVIDER ================== */

    public String generateAccessToken(CustomUserDetails userDetails) {
        return createToken(
                SecurityConstants.JWT_ACCESS_TOKEN,
                userDetails.getId(),
                userDetails.getEmail(),
                SecurityUtils.getAuthorities(userDetails),
                jwtProperties.getAccess().getExpiration()
        );
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        return createToken(
                SecurityConstants.JWT_REFRESH_TOKEN,
                userDetails.getId(),
                userDetails.getEmail(),
                null,
                jwtProperties.getRefresh().getExpiration()
        );
    }

    public String generateResetPasswordToken(long id, String email) {
        return createToken(
                SecurityConstants.JWT_RESET_PASSWORD_TOKEN,
                id,
                email,
                null,
                jwtProperties.getResetPassword().getExpiration()
        );
    }

    public String refreshAccessToken(String refreshToken, CustomUserDetails userDetails) {
        validateRefreshToken(refreshToken);

        return createToken(
                SecurityConstants.JWT_ACCESS_TOKEN,
                userDetails.getId(),
                userDetails.getEmail(),
                SecurityUtils.getAuthorities(userDetails),
                jwtProperties.getAccess().getExpiration()
        );
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, SecurityConstants.JWT_ACCESS_TOKEN);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, SecurityConstants.JWT_REFRESH_TOKEN);
    }

    public boolean validateResetPasswordToken(String token) {
        return validateToken(token, SecurityConstants.JWT_RESET_PASSWORD_TOKEN);
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractEmail(String token) {
        Object email = extractAllClaims(token).get(SecurityConstants.JWT_CLAIMS_EMAIL);
        return email != null ? email.toString() : null;
    }

    public Set<String> extractAuthorities(String token) {
        Object authorities = extractAllClaims(token).get(SecurityConstants.JWT_CLAIMS_AUTHORITIES);
        if (authorities instanceof Collection<?>) {
            return ((Collection<?>) authorities).stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    public Set<String> extractAuthoritiesIgnoreExpiry(String token) {
        Object authorities = extractAllClaimsIgnoreExpiry(token).get(SecurityConstants.JWT_CLAIMS_AUTHORITIES);
        if (authorities instanceof Collection<?>) {
            return ((Collection<?>) authorities).stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    /* ================== PRIVATE HELPERS ================== */

    private String createToken(String type, long id, String email, Set<String> authorities, Duration expiration) {
        Map<String, Object> claims = createClaims(type, email, authorities);
        return buildJwtToken(String.valueOf(id), claims, expiration);
    }

    private String buildJwtToken(String subject, Map<String, Object> claims, Duration expiration) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration.toMillis());
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(now)
                .expiration(expirationDate)
                .issuer(jwtProperties.getIssuer())
                .signWith(secretKey)
                .compact();
    }

    private Map<String, Object> createClaims(String type, String email, Set<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.JWT_CLAIMS_TYPE, type);
        claims.put(SecurityConstants.JWT_CLAIMS_EMAIL, email);
        if (authorities != null) {
            claims.put(SecurityConstants.JWT_CLAIMS_AUTHORITIES, authorities);
        }
        return claims;
    }

    private boolean validateToken(String token, String expectedType) {
        Claims claims = extractAllClaims(token);
        String tokenType = claims.get(SecurityConstants.JWT_CLAIMS_TYPE).toString();

        if (!expectedType.equals(tokenType)) {
            throw TokenException.invalid();
        }

        return true;
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw TokenException.expired();
        } catch (JwtException | IllegalArgumentException e) {
            throw TokenException.invalid();
        }
    }

    private Claims extractAllClaimsIgnoreExpiry(String token) {
        try {
            return extractAllClaims(token);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}