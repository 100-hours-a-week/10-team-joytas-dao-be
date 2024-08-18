package com.example.daobe.user.application.dto;

import com.example.daobe.user.domain.User;

public record UserInfoResponseDto(
        Long userId,
        String nickname,
        String profileUrl
) {

    public static UserInfoResponseDto of(User user) {
        return new UserInfoResponseDto(
                user.getId(),
                user.getNickname(),
                user.getProfileUrl()
        );
    }
}
