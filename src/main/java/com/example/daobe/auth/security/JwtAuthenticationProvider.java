package com.example.daobe.auth.security;

import com.example.daobe.auth.security.jwt.JwtExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtExtractor jwtExtractor;

    @Override
    public Authentication authenticate(
            Authentication authentication
    ) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        String accessToken = token.getAccessToken();

        //  access_token 검증 결과를 가지고 토큰 객체를 만들어 반환
        try {
            Long memberId = jwtExtractor.extractAccessToken(accessToken);
            return JwtAuthenticationToken.afterOf(memberId);
        } catch (RuntimeException ex) {
            throw new RuntimeException("dd");
        }
    }

    // `AuthenticationProvider` 가 처리할 수 있는 객체 타입 지정
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}
