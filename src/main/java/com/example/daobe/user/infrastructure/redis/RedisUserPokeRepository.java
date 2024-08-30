package com.example.daobe.user.infrastructure.redis;

import com.example.daobe.common.utils.DaoStringUtils;
import com.example.daobe.user.domain.event.UserPokeEvent;
import com.example.daobe.user.domain.repository.UserPokeRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUserPokeRepository implements UserPokeRepository {

    private static final Long TTL = 180L;  // FIXME: 임시 TTL 3시간 (180분)
    private static final String POKE_PREFIX = "poke:";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(UserPokeEvent userPokeEvent) {
        String key = generateKey(userPokeEvent.getSendUserId());
        String value = userPokeEvent.getReceiveUserId().toString();
        redisTemplate.opsForValue().set(key, value, TTL, TimeUnit.MINUTES);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        String key = generateKey(userId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private String generateKey(Long userId) {
        return DaoStringUtils.combineToString(POKE_PREFIX, userId);
    }
}
