package com.example.daobe.user.domain.repository.dto;

public record UserSearchCondition(
        String nickname,
        Long cursor,
        int executeLimitSize
) {
}
