package com.example.daobe.notification.infrastructure.redis;

import com.example.daobe.notification.application.NotificationEventListener;
import com.example.daobe.notification.application.dto.NotificationEventPayloadDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisNotificationMessageListener implements MessageListener {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final NotificationEventListener notificationEventListener;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        NotificationEventPayloadDto payload = deserializeFromBytes(message.getBody());
        notificationEventListener.publishToClient(payload);
    }

    private NotificationEventPayloadDto deserializeFromBytes(byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, NotificationEventPayloadDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
