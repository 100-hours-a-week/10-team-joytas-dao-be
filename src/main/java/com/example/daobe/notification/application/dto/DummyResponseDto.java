package com.example.daobe.notification.application.dto;

public record DummyResponseDto(
        String message
) {

    public static DummyResponseDto of() {
        return new DummyResponseDto("connect success");
    }
}
