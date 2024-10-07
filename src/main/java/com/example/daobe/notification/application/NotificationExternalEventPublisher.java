package com.example.daobe.notification.application;

import com.example.daobe.notification.application.dto.NotificationEventPayloadDto;

public interface NotificationEventPublisher {

    void execute(NotificationEventPayloadDto payload);
}
