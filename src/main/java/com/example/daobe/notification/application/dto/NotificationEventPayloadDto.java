package com.example.daobe.notification.application.dto;

public record NotificationEventPayloadDto(
        Long receiveUserId,
        NotificationInfoResponseDto data
) {

    public static NotificationEventPayloadDto of(Long receiveUserId, NotificationInfoResponseDto data) {
        return new NotificationEventPayloadDto(receiveUserId, data);
    }
}
