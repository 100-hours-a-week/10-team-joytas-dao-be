package com.example.daobe.myroom.exception;

import com.example.daobe.common.exception.BaseException;

public class MyRoomException extends BaseException {

    public MyRoomException(MyRoomExceptionType exceptionType) {
        super(exceptionType);
    }
}
