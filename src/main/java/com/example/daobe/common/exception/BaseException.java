package com.example.daobe.common.exception;

public class BaseException extends RuntimeException {

    private final BaseExceptionType baseExceptionType;

    public BaseException(BaseExceptionType baseExceptionType) {
        super(baseExceptionType.message());
        this.baseExceptionType = baseExceptionType;
    }

    public BaseExceptionType getType() {
        return baseExceptionType;
    }
}
