package com.example.daobe.user.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum UserExceptionType implements BaseExceptionType {
    DISABLED_USER("삭제된 사용자입니다.", HttpStatus.NOT_FOUND),
    NOT_EXIST_USER("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_NICKNAME("이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT),
    ALREADY_POKE("콕 찌르기는 3시간에 한번 가능합니다.", HttpStatus.BAD_REQUEST),
    NOT_MATCH_REASON_TYPE("일치하는 사유 타입이 없습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus status;

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
