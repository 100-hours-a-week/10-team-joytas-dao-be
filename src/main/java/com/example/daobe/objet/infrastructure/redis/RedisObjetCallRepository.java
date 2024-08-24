package com.example.daobe.objet.infrastructure.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisObjetCallRepository implements ObjetCallRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisObjetCallRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long getObjetLength(Long objetId) {
        // Redis에서 objet:{objetId} 키의 리스트 길이를 조회
        String key = "objet:" + objetId;
        return redisTemplate.opsForList().size(key);
    }
}
