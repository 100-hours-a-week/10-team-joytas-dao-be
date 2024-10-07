package com.example.daobe.objet.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum ObjetExceptionType implements BaseExceptionType {
    OBJET_NOT_FOUND_EXCEPTION("존재하지 않는 오브제입니다.", HttpStatus.NOT_FOUND),
    NO_PERMISSIONS_ON_OBJET("오브제에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOT_EXISTS_OBJET_TYPE("존재하지 않는 오브제 타입입니다.", HttpStatus.NOT_FOUND),
    ;

    private final String message;
    private final HttpStatus status;

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
