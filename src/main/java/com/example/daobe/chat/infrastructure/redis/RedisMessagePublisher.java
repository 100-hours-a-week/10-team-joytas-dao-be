package com.example.daobe.chat.infrastructure.redis;

import com.example.daobe.chat.application.ChatMessageEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessagePublisher implements ChatMessageEventPublisher {

    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> void execute(T message) {
        String topic = channelTopic.getTopic();
        redisTemplate.convertAndSend(topic, message);
    }
}
