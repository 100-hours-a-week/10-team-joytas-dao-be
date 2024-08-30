package com.example.daobe.objet.infrastructure.redis;

import com.example.daobe.common.utils.DaoStringUtils;
import com.example.daobe.objet.domain.repository.ObjetCallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisObjetCallRepository implements ObjetCallRepository {

    private static final String OBJET_PREFIX = "objet:";

    private final RedisTemplate<String, String> redisTemplate;

    public Long getObjetLength(Long objetId) {
        String key = generateKey(objetId);
        return redisTemplate.opsForList().size(key);
    }

    private String generateKey(Long objetId) {
        return DaoStringUtils.combineToString(OBJET_PREFIX, objetId);
    }
}
