package com.example.daobe.common.response;

public record ApiResponse<T>(
        String message,
        T data
) {
}
