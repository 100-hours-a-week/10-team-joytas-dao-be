package com.example.daobe.auth.presentation;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.example.daobe.auth.application.AuthService;
import com.example.daobe.auth.application.dto.TokenResponseDto;
import com.example.daobe.common.presentation.cookie.CookieHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final String COOKIE_REFRESH_TOKEN = "refresh_token";

    private final AuthService authService;
    private final CookieHandler cookieHandler;

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(
            @CookieValue(COOKIE_REFRESH_TOKEN) String refreshToken,
            HttpServletResponse response
    ) {
        TokenResponseDto tokenPairResponse = authService.reissueTokenPair(refreshToken);
        ResponseCookie cookie = cookieHandler.createCookie(
                COOKIE_REFRESH_TOKEN, tokenPairResponse.refreshToken()
        );
        response.addHeader(SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(tokenPairResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal Long userId,
            @CookieValue(COOKIE_REFRESH_TOKEN) String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(userId, refreshToken);
        ResponseCookie cookie = cookieHandler.deleteCookie(COOKIE_REFRESH_TOKEN);
        response.addHeader(SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(null);
    }
}
