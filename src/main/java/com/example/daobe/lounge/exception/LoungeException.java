package com.example.daobe.lounge.exception;

import com.example.daobe.common.exception.BaseException;

public class LoungeException extends BaseException {

    public LoungeException(LoungeExceptionType exceptionType) {
        super(exceptionType);
    }
}
