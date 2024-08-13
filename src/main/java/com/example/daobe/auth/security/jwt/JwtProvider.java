package com.example.daobe.auth.security.jwt;

import com.example.daobe.auth.service.TokenProvider;
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

    private static final String MEMBER_ID = "member_id";
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
    public String generatedAccessToken(Long memberId) {
        Claims claims = generatedClaims(MEMBER_ID, memberId);
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
