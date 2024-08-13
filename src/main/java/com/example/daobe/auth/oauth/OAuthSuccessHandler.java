package com.example.daobe.auth.oauth;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.example.daobe.auth.dto.TokenResponseDto;
import com.example.daobe.auth.service.AuthService;
import com.example.daobe.common.controller.cookie.CookieHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String FRONTEND_URL = "http://localhost:5173";  // FIXME: 프론트엔드 주소 변경

    private final CookieHandler cookieHandler;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String kakaoId = oAuth2User.getName();
        TokenResponseDto tokenResponseDto = authService.generateTokenPair(kakaoId);
        ResponseCookie cookie = cookieHandler.createCookie(
                REFRESH_TOKEN, tokenResponseDto.refreshToken()
        );
        response.addHeader(SET_COOKIE, cookie.toString());
        response.sendRedirect(FRONTEND_URL);
    }
}
