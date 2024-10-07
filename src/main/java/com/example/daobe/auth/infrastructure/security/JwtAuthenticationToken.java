package com.example.daobe.auth.infrastructure.security;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final String EMPTY_CREDENTIALS = "";

    public JwtAuthenticationToken(
            Object principal,
            Object credentials
    ) {
        super(principal, credentials);
    }

    public JwtAuthenticationToken(
            Object principal,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(principal, credentials, authorities);
    }

    public static JwtAuthenticationToken beforeOf(String accessToken) {
        return new JwtAuthenticationToken(accessToken, EMPTY_CREDENTIALS);
    }

    public static JwtAuthenticationToken afterOf(Long userId) {
        return new JwtAuthenticationToken(userId, EMPTY_CREDENTIALS, null);
    }

    public String getAccessToken() {
        return (String) this.getPrincipal();
    }
}
