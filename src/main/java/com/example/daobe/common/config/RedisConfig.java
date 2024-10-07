package com.example.daobe.common.config;

import com.example.daobe.chat.infrastructure.redis.RedisChatMessageListener;
import com.example.daobe.notification.infrastructure.redis.RedisNotificationMessageListener;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

    private static final String CHATTING_CHANNEL_TOPIC = "chatting:channel";
    private static final String NOTIFICATION_CHANNEL_TOPIC = "notification:channel";

    private final RedisProperties redisProperties;
    private final RedisChatMessageListener chatMessageListener;
    private final RedisNotificationMessageListener notificationMessageListener;

    @Bean
    public RedisClient redisClient() {
        RedisURI redisUri = RedisURI.Builder.redis(redisProperties.getHost())
                .withPort(redisProperties.getPort())
                .build();
        return RedisClient.create(redisUri);
    }

    @Bean
    public StatefulRedisConnection<String, byte[]> statefulRedisConnection(RedisClient redisClient) {
        return redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        notificationSubscribe(container);
        chatSubscribe(container);
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }

    private void notificationSubscribe(RedisMessageListenerContainer container) {
        container.addMessageListener(
                notificationMessageListener,
                new ChannelTopic(NOTIFICATION_CHANNEL_TOPIC)
        );
    }

    private void chatSubscribe(RedisMessageListenerContainer container) {
        container.addMessageListener(
                chatMessageListener,
                new ChannelTopic(CHATTING_CHANNEL_TOPIC)
        );
    }
}
