package com.example.daobe.auth.infrastructure.security.oauth;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.example.daobe.auth.application.AuthService;
import com.example.daobe.auth.application.dto.TokenResponseDto;
import com.example.daobe.common.presentation.cookie.CookieHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REFRESH_TOKEN = "refresh_token";

    private final CookieHandler cookieHandler;
    private final AuthService authService;
    private final String successRedirectUrl;

    public OAuthSuccessHandler(
            CookieHandler cookieHandler,
            AuthService authService,
            @Value("${spring.security.oauth2.success-redirect}") String successRedirectUrl
    ) {
        this.cookieHandler = cookieHandler;
        this.authService = authService;
        this.successRedirectUrl = successRedirectUrl;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        TokenResponseDto tokenResponseDto = authService.loginOrRegister(oAuth2User.getName());
        ResponseCookie cookie = cookieHandler.createCookie(
                REFRESH_TOKEN,
                tokenResponseDto.refreshToken()
        );
        response.addHeader(SET_COOKIE, cookie.toString());
        response.sendRedirect(successRedirectUrl);
    }
}
