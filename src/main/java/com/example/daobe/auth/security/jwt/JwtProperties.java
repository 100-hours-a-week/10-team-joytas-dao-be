package com.example.daobe.auth.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secretKey,
        Long accessExpired,
        Long refreshExpired
) {
}
