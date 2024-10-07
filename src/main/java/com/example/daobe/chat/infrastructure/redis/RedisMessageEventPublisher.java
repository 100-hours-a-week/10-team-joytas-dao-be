package com.example.daobe.chat.infrastructure.redis;

import com.example.daobe.chat.application.ChatMessageEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageEventPublisher implements ChatMessageEventPublisher {

    private static final String CHAT_CHANNEL_TOPIC = "chatting:channel";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> void execute(T message) {
        redisTemplate.convertAndSend(CHAT_CHANNEL_TOPIC, message);
    }
}
