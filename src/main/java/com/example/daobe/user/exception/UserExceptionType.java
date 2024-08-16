package com.example.daobe.user.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum UserExceptionType implements BaseExceptionType {
    INVALID_USER_ID_EXCEPTION("유효하지 않은 유저 ID입니다.", HttpStatus.NOT_FOUND),
    ;

    private String message;
    private HttpStatus status;

    UserExceptionType(String message, HttpStatus status) {
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
