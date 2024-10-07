package com.example.daobe.user.exception;

import com.example.daobe.common.exception.BaseException;

public class UserException extends BaseException {

    public UserException(UserExceptionType exceptionType) {
        super(exceptionType);
    }
}
