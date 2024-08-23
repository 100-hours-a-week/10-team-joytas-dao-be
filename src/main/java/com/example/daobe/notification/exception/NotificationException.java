package com.example.daobe.notification.exception;

import com.example.daobe.common.exception.BaseException;

public class NotificationException extends BaseException {

    public NotificationException(NotificationExceptionType exceptionType) {
        super(exceptionType);
    }
}
