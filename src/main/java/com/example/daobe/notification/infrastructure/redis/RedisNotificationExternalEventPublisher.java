package com.example.daobe.notification.infrastructure.redis;

import com.example.daobe.notification.application.NotificationExternalEventPublisher;
import com.example.daobe.notification.application.dto.NotificationEventPayloadDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisNotificationExternalEventPublisher implements NotificationExternalEventPublisher {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String NOTIFICATION_CHANNEL_TOPIC = "notification:channel";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void execute(NotificationEventPayloadDto payload) {
        redisTemplate.convertAndSend(
                NOTIFICATION_CHANNEL_TOPIC,
                serializeToString(payload)
        );
    }

    private String serializeToString(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
