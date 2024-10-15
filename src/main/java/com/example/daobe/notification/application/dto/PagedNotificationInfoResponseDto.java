package com.example.daobe.notification.application.dto;

import java.util.List;

public record PagedNotificationInfoResponseDto(
        boolean hasNext,
        Object nextCursor,
        List<NotificationInfoResponseDto> notifications
) {

    public static PagedNotificationInfoResponseDto of(
            boolean hasNext,
            List<NotificationInfoResponseDto> notificationInfoList
    ) {
        return new PagedNotificationInfoResponseDto(hasNext, null, notificationInfoList);
    }

    public static PagedNotificationInfoResponseDto of(
            boolean hasNext,
            Object nextCursor,
            List<NotificationInfoResponseDto> notificationInfoList
    ) {
        return new PagedNotificationInfoResponseDto(hasNext, nextCursor, notificationInfoList);
    }
}
