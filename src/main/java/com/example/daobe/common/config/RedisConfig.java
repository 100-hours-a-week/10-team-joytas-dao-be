package com.example.daobe.common.config;

import com.example.daobe.auth.domain.Token;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Token> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Token> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
}
