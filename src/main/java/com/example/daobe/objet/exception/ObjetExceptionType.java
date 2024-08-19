package com.example.daobe.objet.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum ObjetExceptionType implements BaseExceptionType {
    INVALID_OBJET_ID_EXCEPTION("유효하지 않은 오브제 ID입니다.", HttpStatus.NOT_FOUND),
    NO_PERMISSIONS_ON_OBJET("오브제에 대한 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_OBJET_IMAGE_EXTENSIONS("오브제 이미지의 확장자가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_OBJET_IMAGE("오브제 이미지가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
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
