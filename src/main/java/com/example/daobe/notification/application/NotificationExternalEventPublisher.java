package com.example.daobe.notification.application;

import com.example.daobe.notification.application.dto.NotificationEventPayloadDto;

public interface NotificationExternalEventPublisher {

    void execute(NotificationEventPayloadDto payload);
}
