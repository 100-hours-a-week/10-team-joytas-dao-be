package com.example.daobe.auth.infrastructure.security;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

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

    // 인증 전의 토큰 객체 생성
    public static JwtAuthenticationToken beforeOf(String accessToken) {
        return new JwtAuthenticationToken(accessToken, "");
    }

    // 인증 후의 토큰 객체 생성
    public static JwtAuthenticationToken afterOf(Long memberId) {
        return new JwtAuthenticationToken(memberId, "", null);
    }

    public String getAccessToken() {
        return (String) this.getPrincipal();
    }
}
