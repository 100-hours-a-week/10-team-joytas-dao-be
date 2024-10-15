package com.example.daobe.user.application.dto;

import com.example.daobe.user.domain.User;
import java.util.List;

public record PagedUserInfoResponseDto(
        boolean hasNext,
        Object nextCursor,
        List<UserInfoResponseDto> users
) {

    public static PagedUserInfoResponseDto of(boolean hasNext, List<User> userList) {
        return new PagedUserInfoResponseDto(
                hasNext,
                null,
                userList.stream()
                        .map(UserInfoResponseDto::of)
                        .toList()
        );
    }

    public static PagedUserInfoResponseDto of(boolean hasNext, Long nextCursor, List<User> userList) {
        return new PagedUserInfoResponseDto(
                hasNext,
                nextCursor,
                userList.stream()
                        .map(UserInfoResponseDto::of)
                        .toList()
        );
    }
}
