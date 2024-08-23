package com.example.daobe.notification.application.dto;

import com.example.daobe.common.domain.DomainEvent;

public record NotificationPayload(
        Long receiveUserId,
        Long sendUserId,
        String eventType
) {

    public static NotificationPayload of(DomainEvent domainEvent, String eventType) {
        return new NotificationPayload(
                domainEvent.getReceiveUserId(),
                domainEvent.getSendUserId(),
                eventType
        );
    }
}
