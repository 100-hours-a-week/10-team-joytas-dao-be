package com.example.daobe.lounge.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum LoungeExceptionType implements BaseExceptionType {
    INVALID_LOUNGE_ID_EXCEPTION("유효하지 않은 라운지 ID입니다.", HttpStatus.NOT_FOUND),
    ;

    private String message;
    private HttpStatus status;

    LoungeExceptionType(String message, HttpStatus status) {
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
