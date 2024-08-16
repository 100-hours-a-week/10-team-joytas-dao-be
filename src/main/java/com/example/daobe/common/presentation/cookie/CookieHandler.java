package com.example.daobe.common.presentation.cookie;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieHandler {

    private static final Long DELETE_COOKIE_MAX_AGE = 0L;
    private static final String DELETE_COOKIE_VALUE = "";

    private final CookieProperties properties;

    /**
     * 쿠키를 생성합니다.
     *
     * @param cookieKey   쿠키의 키
     * @param cookieValue 쿠키의 값
     * @return 생성된 ResponseCookie 객체
     * @author JiHongKim98
     */
    public ResponseCookie createCookie(String cookieKey, String cookieValue) {
        return createCookieWithMaxAge(cookieKey, cookieValue, properties.maxAge());
    }

    /**
     * 지정된 만료 시간을 가진 쿠키를 생성합니다.
     *
     * @param cookieKey   쿠키의 키
     * @param cookieValue 쿠키의 값
     * @param maxAge      쿠키의 최대 수명 (초 단위)
     * @return 생성된 ResponseCookie 객체
     * @author JiHongKim98
     */
    public ResponseCookie createCookie(String cookieKey, String cookieValue, Long maxAge) {
        return createCookieWithMaxAge(cookieKey, cookieValue, maxAge);
    }

    /**
     * 쿠키를 삭제합니다.
     *
     * @param cookieKey 삭제할 쿠키의 키
     * @return 삭제를 위한 ResponseCookie 객체
     * @author JiHongKim98
     */
    public ResponseCookie deleteCookie(String cookieKey) {
        return createCookieWithMaxAge(cookieKey, DELETE_COOKIE_VALUE, DELETE_COOKIE_MAX_AGE);
    }

    private ResponseCookie createCookieWithMaxAge(String cookieKey, String cookieValue, Long maxAge) {
        return ResponseCookie.from(cookieKey, cookieValue)
                .maxAge(maxAge)
                .path(properties.path())
                .sameSite(properties.sameSite())
                .secure(properties.secure())
                .httpOnly(properties.httpOnly())
                .domain(properties.domain())
                .build();
    }
}
