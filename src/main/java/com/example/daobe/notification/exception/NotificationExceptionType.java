package com.example.daobe.notification.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum NotificationExceptionType implements BaseExceptionType {
    NON_MATCH_DOMAIN_EVENT_TYPE("일치하는 도메인 타입이 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    IS_NOT_SINGLE_EMITTER("생성된 이미터가 1개가 아닙니다", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final HttpStatus status;

    NotificationExceptionType(String message, HttpStatus status) {
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
