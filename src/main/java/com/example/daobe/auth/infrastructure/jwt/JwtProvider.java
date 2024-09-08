package com.example.daobe.auth.infrastructure.jwt;

import com.example.daobe.auth.application.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider implements TokenProvider {

    private static final String USER_ID = "user_id";
    private static final String TOKEN_ID = "token_id";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final SecretKey key;
    private final JwtProperties jwtProperties;

    public JwtProvider(
            JwtProperties jwtProperties
    ) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String generatedAccessToken(Long userId) {
        Claims claims = generatedClaims(USER_ID, userId);
        return generatedToken(claims, ACCESS_TOKEN, jwtProperties.accessExpired());
    }

    @Override
    public String generatedRefreshToken(String tokenId) {
        Claims claims = generatedClaims(TOKEN_ID, tokenId);
        return generatedToken(claims, REFRESH_TOKEN, jwtProperties.refreshExpired());
    }

    private Claims generatedClaims(String key, Object value) {
        Claims claims = Jwts.claims();
        claims.put(key, value);
        return claims;
    }

    private String generatedToken(Claims claims, String subject, Long exp) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
