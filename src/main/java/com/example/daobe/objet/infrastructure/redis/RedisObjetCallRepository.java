package com.example.daobe.objet.infrastructure.redis;

import com.example.daobe.objet.domain.repository.ObjetCallRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisObjetCallRepository implements ObjetCallRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisObjetCallRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long getObjetLength(Long objetId) {
        // Redis에서 objet:{objetId} 키의 리스트 길이를 조회
        String key = "objet:" + objetId;
        return redisTemplate.opsForList().size(key);
    }
}
