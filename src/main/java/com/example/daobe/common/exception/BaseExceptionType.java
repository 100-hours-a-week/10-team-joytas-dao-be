package com.example.daobe.common.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    String message();

    HttpStatus status();
}
