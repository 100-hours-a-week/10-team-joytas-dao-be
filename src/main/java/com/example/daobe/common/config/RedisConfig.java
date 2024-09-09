package com.example.daobe.common.config;

import com.example.daobe.notification.infrastructure.redis.RedisNotificationMessageListener;
import lombok.RequiredArgsConstructor;
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

    private final RedisNotificationMessageListener notificationMessageListener;

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
