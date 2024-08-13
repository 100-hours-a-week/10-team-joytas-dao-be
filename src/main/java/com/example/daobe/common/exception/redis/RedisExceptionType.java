package com.example.daobe.common.exception.redis;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum RedisExceptionType implements BaseExceptionType {
    SERIALIZE_ERROR("데이터 직렬화 실패 오류", HttpStatus.INTERNAL_SERVER_ERROR),
    DESERIALIZE_ERROR("데이터 역직렬화 실패 오류", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final HttpStatus status;

    RedisExceptionType(String message, HttpStatus status) {
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
