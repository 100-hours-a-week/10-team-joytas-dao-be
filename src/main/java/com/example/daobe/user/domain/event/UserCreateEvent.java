package com.example.daobe.user.domain.event;

import com.example.daobe.user.domain.User;

public record UserCreateEvent(
        Long userId,
        String nickname,
        String profileUrl
) {

    public static UserCreateEvent of(User user) {
        return new UserCreateEvent(
                user.getId(),
                user.getNickname(),
                user.getProfileUrl()
        );
    }
}
