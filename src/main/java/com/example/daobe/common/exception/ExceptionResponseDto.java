package com.example.daobe.common.exception;

public record ExceptionResponseDto(
        int status,
        String message
) {
}
