package com.example.daobe.notification.domain.repository.dto;

public record NotificationFindCondition(
        Long userId,
        Long cursor,
        int executeLimitSize
) {
}
