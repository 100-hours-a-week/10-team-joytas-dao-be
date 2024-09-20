package com.example.daobe.auth.infrastructure.jwt;

import static com.example.daobe.auth.exception.AuthExceptionType.ALREADY_EXPIRED_TOKEN;
import static com.example.daobe.auth.exception.AuthExceptionType.INVALID_TOKEN;
import static com.example.daobe.auth.exception.AuthExceptionType.INVALID_TOKEN_TYPE;

import com.example.daobe.auth.application.TokenExtractor;
import com.example.daobe.auth.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtExtractor implements TokenExtractor {

    private static final String USER_ID = "user_id";
    private static final String TOKEN_ID = "token_id";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final JwtParser jwtParser;

    public JwtExtractor(JwtProperties jwtProperties) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    @Override
    public Long extractAccessToken(String token) {
        return extract(token, ACCESS_TOKEN, USER_ID, Long.class);
    }

    @Override
    public String extractRefreshToken(String token) {
        return extract(token, REFRESH_TOKEN, TOKEN_ID, String.class);
    }

    private <T> T extract(String token, String expectedTokenType, String claimKey, Class<T> T) {
        Claims claims = parseClaim(token);
        String subject = claims.getSubject();
        T claimValue = claims.get(claimKey, T);

        if (claimValue != null && subject.equals(expectedTokenType)) {
            return claimValue;
        }
        throw new AuthException(INVALID_TOKEN_TYPE);
    }

    private Claims parseClaim(String token) {
        try {
            return jwtParser.parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new AuthException(ALREADY_EXPIRED_TOKEN);
        } catch (Exception ex) {
            throw new AuthException(INVALID_TOKEN);
        }
    }
}
