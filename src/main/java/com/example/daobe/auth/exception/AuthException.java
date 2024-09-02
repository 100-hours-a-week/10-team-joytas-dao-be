package com.example.daobe.auth.exception;

import com.example.daobe.common.exception.BaseException;

public class AuthException extends BaseException {

    public AuthException(AuthExceptionType exceptionType) {
        super(exceptionType);
    }
}
