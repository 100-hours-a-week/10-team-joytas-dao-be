package com.example.daobe.notification.application;

import com.example.daobe.notification.application.dto.NotificationEventPayloadDto;

public interface NotificationEventPublisher {

    void publishNotificationEvent(NotificationEventPayloadDto payload);
}
