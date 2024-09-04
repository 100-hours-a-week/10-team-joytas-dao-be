package com.example.daobe.user.domain.event;

import com.example.daobe.user.domain.User;

public record UserUpdateEvent(
        Long userId,
        String nickname,
        String profileUrl
) {

    public static UserUpdateEvent of(User user) {
        return new UserUpdateEvent(
                user.getId(),
                user.getNickname(),
                user.getProfileUrl()
        );
    }
}
