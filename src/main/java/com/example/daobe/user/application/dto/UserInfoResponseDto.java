package com.example.daobe.user.application.dto;

import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.UserStatus;

public record UserInfoResponseDto(
        Long userId,
        UserStatus userStatus,
        String nickname,
        String profileUrl
) {

    public static UserInfoResponseDto of(User user) {
        return new UserInfoResponseDto(
                user.getId(),
                user.getStatus(),
                user.getNickname(),
                user.getProfileUrl()
        );
    }
}
