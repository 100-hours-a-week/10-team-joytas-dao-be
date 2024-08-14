package com.example.daobe.common.exception;

public record ExceptionResponse(
        int status,
        String message
) {
}
