package com.example.daobe.objet.infrastructure.redis;

import com.example.daobe.objet.domain.repository.ObjetCallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisObjetCallRepository implements ObjetCallRepository {

    private static final String OBJET_PREFIX = "objet:";

    private final RedisTemplate<String, String> redisTemplate;

    // redis 에서 objet:{objetId} 키의 리스트 길이를 조회
    public Long getObjetLength(Long objetId) {
        return redisTemplate.opsForList().size(key(objetId));
    }

    private String key(Long objetId) {
        return OBJET_PREFIX + objetId;
    }
}
