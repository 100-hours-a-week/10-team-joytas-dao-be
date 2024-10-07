package com.example.daobe.common.config;

import com.example.daobe.common.throttling.RateLimitInterceptor;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.api.StatefulRedisConnection;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RateLimitConfig implements WebMvcConfigurer {

    private static final Duration DEFAULT_TTL = Duration.ofSeconds(30);

    private final StatefulRedisConnection<String, byte[]> connection;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(proxyManager()))
                .addPathPatterns("/**");
    }

    private LettuceBasedProxyManager<String> proxyManager() {
        return Bucket4jLettuce
                .casBasedBuilder(connection)
                .expirationAfterWrite(ExpirationAfterWriteStrategy.fixedTimeToLive(DEFAULT_TTL))
                .build();
    }
}
