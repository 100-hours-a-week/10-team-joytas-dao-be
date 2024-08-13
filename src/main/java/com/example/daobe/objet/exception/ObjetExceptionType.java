package com.example.daobe.objet.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum ObjetExceptionType implements BaseExceptionType {
    NOT_EXIST_USER("존재하지 않는 유저입니다", HttpStatus.NOT_FOUND),
    ;

    private String message;
    private HttpStatus status;

    ObjetExceptionType(String message, HttpStatus status) {
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
