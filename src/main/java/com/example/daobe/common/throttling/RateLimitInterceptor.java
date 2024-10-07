package com.example.daobe.common.throttling;

import com.example.daobe.common.throttling.annotation.RateLimited;
import com.example.daobe.common.utils.DaoStringUtils;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String DETERMINE = ":";
    private static final int CONSUME_TOKEN_COUNT = 1;
    private static final String RATE_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    private static final String RATE_LIMIT_RETRY_AFTER = "X-Rate-Limit-Retry-After";
    private static final String RATE_LIMIT_USER_ID_ERROR_MESSAGE =
            "Rate limiting is enabled but failed to extract userId from security context";

    private final LettuceBasedProxyManager<String> proxyManager;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        RateLimited rateLimited = extractRateLimitedAnnotation(handler);

        if (rateLimited != null) {
            Long userId = extractUserIdFromSecurityContext();

            if (userId != null) {
                BucketConfiguration configuration = BucketConfiguration.builder()
                        .addLimit(limit -> limit.capacity(rateLimited.capacity()).refillGreedy(
                                rateLimited.refillTokens(),
                                Duration.ofSeconds(rateLimited.refillSeconds())
                        ))
                        .build();
                BucketProxy bucket = proxyManager.getProxy(
                        generateRateLimiterName(rateLimited.name(), userId),
                        () -> configuration
                );

                ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(CONSUME_TOKEN_COUNT);
                if (!probe.isConsumed()) {
                    long waitForRefillSeconds = TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill());
                    response.setHeader(RATE_LIMIT_RETRY_AFTER, String.valueOf(waitForRefillSeconds));
                    throw new RateLimitException(RateLimitExceptionType.THROTTLING_EXCEPTION);
                }

                long remainingTokens = probe.getRemainingTokens();
                response.setHeader(RATE_LIMIT_REMAINING, String.valueOf(remainingTokens));
                return true;
            }
            throw new RuntimeException(RATE_LIMIT_USER_ID_ERROR_MESSAGE);
        }
        return true;
    }

    private String generateRateLimiterName(String rateLimiterName, Long userId) {
        return DaoStringUtils.combineToString(rateLimiterName, DETERMINE, userId);
    }

    private Long extractUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }

    private RateLimited extractRateLimitedAnnotation(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            return handlerMethod.getMethodAnnotation(RateLimited.class);
        }
        return null;
    }
}

