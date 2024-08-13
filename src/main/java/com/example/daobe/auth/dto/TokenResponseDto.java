package com.example.daobe.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TokenResponseDto(
        String accessToken,
        @JsonIgnore String refreshToken
) {

    public static TokenResponseDto of(String accessToken, String refreshToken) {
        return new TokenResponseDto(accessToken, refreshToken);
    }
}
