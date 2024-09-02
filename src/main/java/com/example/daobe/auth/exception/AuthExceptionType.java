package com.example.daobe.auth.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum AuthExceptionType implements BaseExceptionType {
    INVALID_TOKEN("유효하지 않은 토큰 입니다", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN_TYPE("유효하지 않은 토큰 타입 입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXPIRED_TOKEN("이미 만료된 토큰 입니다.", HttpStatus.UNAUTHORIZED),
    UN_MATCH_USER_INFO("토큰에 저장된 사용자와 일치하지 않는 사용자입니다.", HttpStatus.UNAUTHORIZED),
    ;

    private final String message;
    private final HttpStatus status;

    AuthExceptionType(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus status() {
        return status;
    }
}
