package com.example.daobe.notification.application.dto;

import com.example.daobe.common.domain.DomainEvent;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificationPayload(
        @JsonProperty("type_id") Long typeId,
        @JsonProperty("receive_user_id") Long receiveUserId,
        @JsonProperty("send_user_id") Long sendUserId,
        @JsonProperty("event_type") String eventType
) {

    public static NotificationPayload of(DomainEvent domainEvent, String eventType) {
        return new NotificationPayload(
                domainEvent.getDomainId(),
                domainEvent.getReceiveUserId(),
                domainEvent.getSendUserId(),
                eventType
        );
    }
}
