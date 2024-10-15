package com.example.daobe.common.presentation.response;

import java.time.LocalDateTime;

public record ApiResponseDto<T>(
        String path,
        T data,
        LocalDateTime timestamp
) {

    public static <T> ApiResponseDto<T> of(String path, T data) {
        return new ApiResponseDto<>(path, data, LocalDateTime.now());
    }
}
