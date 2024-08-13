package com.example.daobe.common.advice;

public record ExceptionResponse(
        int status,
        String message
) {
}
