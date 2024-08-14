package com.example.daobe.auth.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.example.daobe.auth.dto.TokenResponseDto;
import com.example.daobe.auth.service.AuthService;
import com.example.daobe.common.controller.cookie.CookieHandler;
import com.example.daobe.common.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<TokenResponseDto>> reissue(
            @CookieValue(COOKIE_REFRESH_TOKEN) String refreshToken,
            HttpServletResponse response
    ) {
        TokenResponseDto tokenPair = authService.reissueTokenPair(refreshToken);
        ResponseCookie cookie = cookieHandler.createCookie(
                COOKIE_REFRESH_TOKEN, tokenPair.refreshToken()
        );
        response.addHeader(SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new ApiResponse<>(
                "REISSUE_SUCCESS", tokenPair
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal Long userId,
            @CookieValue(COOKIE_REFRESH_TOKEN) String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(userId, refreshToken);
        ResponseCookie cookie = cookieHandler.deleteCookie(COOKIE_REFRESH_TOKEN);
        response.addHeader(SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new ApiResponse<>(
                "LOGOUT_SUCCESS", null
        ));
    }
}
