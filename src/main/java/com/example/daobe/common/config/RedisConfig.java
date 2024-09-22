package com.example.daobe.common.config;

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
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

    private static final String NOTIFICATION_CHANNEL_TOPIC = "notification:channel";

    private final RedisProperties redisProperties;
    private final RedisNotificationMessageListener notificationMessageListener;

    @Bean
    public StatefulRedisConnection<String, byte[]> redisClient() {
        RedisURI redisUri = RedisURI.Builder.redis(redisProperties.getHost())
                .withPort(redisProperties.getPort())
                .build();
        RedisClient redisClient = RedisClient.create(redisUri);
        return redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
    }

    @Bean
    public RedisMessageListenerContainer notificationContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        notificationSubscribe(container);
        return container;
    }

    private void notificationSubscribe(RedisMessageListenerContainer container) {
        container.addMessageListener(
                notificationMessageListener,
                new ChannelTopic(NOTIFICATION_CHANNEL_TOPIC)
        );
    }
}
