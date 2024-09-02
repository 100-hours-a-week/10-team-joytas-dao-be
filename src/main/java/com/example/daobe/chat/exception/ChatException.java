package com.example.daobe.chat.exception;

import com.example.daobe.common.exception.BaseException;
import com.example.daobe.common.exception.BaseExceptionType;

public class ChatException extends BaseException {

    public ChatException(BaseExceptionType baseExceptionType) {
        super(baseExceptionType);
    }
}
