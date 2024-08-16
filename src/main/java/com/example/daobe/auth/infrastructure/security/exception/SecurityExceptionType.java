package com.example.daobe.auth.infrastructure.security.exception;

public enum SecurityExceptionType {
    UNAUTHORIZED("인증 정보가 제공되지 않았습니다."),
    INVALID_TOKEN("유효하지 않는 토큰입니다."),
    EXPIRED_TOKEN("이미 만료된 토큰입니다."),
    ;

    private final String message;

    SecurityExceptionType(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
