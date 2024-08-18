package com.example.daobe.user.application.dto;

import com.example.daobe.user.domain.User;

public record UpdateProfileResponseDto(
        Long userId
) {

    public static UpdateProfileResponseDto of(User user) {
        return new UpdateProfileResponseDto(user.getId());
    }
}
