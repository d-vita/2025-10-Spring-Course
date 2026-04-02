package com.urlshortener.repository.cache;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

import static com.urlshortener.constants.Constants.*;

@Repository
@AllArgsConstructor
public class RedisRepository implements CacheRepository<String, String> {

    private final RedisTemplate<String, String> redisTemplate;
    @Override
    public void save(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(SHORT_KEY_PREFIX + key, value, ttl);
        redisTemplate.opsForValue().set(LONG_KEY_PREFIX + value, key, ttl);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(SHORT_KEY_PREFIX + key);
    }

    @Override
    public String getByValue(String value) {
        return redisTemplate.opsForValue().get(LONG_KEY_PREFIX + value);
    }

    @Override
    public boolean contains(String key) {
        return redisTemplate.hasKey(SHORT_KEY_PREFIX + key);
    }
}
