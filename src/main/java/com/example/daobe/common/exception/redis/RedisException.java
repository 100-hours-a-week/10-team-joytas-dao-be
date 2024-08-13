package com.example.daobe.common.exception.redis;

import com.example.daobe.common.exception.BaseException;
import com.example.daobe.common.exception.BaseExceptionType;

public class RedisException extends BaseException {

    public RedisException(BaseExceptionType baseExceptionType) {
        super(baseExceptionType);
    }
}
