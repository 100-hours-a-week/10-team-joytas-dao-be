package com.example.daobe.user.exception;

import com.example.daobe.common.exception.BaseException;
import com.example.daobe.common.exception.BaseExceptionType;

public class UserException extends BaseException {

    public UserException(BaseExceptionType baseExceptionType) {
        super(baseExceptionType);
    }
}
