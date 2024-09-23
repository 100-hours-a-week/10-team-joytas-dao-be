package com.example.daobe.common.throttling;

import com.example.daobe.common.exception.BaseException;

public class RateLimitException extends BaseException {

    public RateLimitException(RateLimitExceptionType exceptionType) {
        super(exceptionType);
    }
}
