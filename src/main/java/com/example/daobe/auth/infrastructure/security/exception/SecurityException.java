package com.example.daobe.auth.infrastructure.security.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class SecurityException extends AuthenticationException {

    public SecurityException(SecurityExceptionType exceptionType) {
        super(exceptionType.message());
    }

    public SecurityException(SecurityExceptionType exceptionType, Throwable cause) {
        super(exceptionType.message(), cause);
    }
}
