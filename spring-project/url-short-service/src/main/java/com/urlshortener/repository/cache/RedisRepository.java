package com.urlshortener.repository.cache;

import com.urlshortener.dto.UrlCacheDto;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

import static com.urlshortener.constants.Constants.LONG_KEY_PREFIX;
import static com.urlshortener.constants.Constants.SHORT_KEY_PREFIX;


@Repository
@AllArgsConstructor
public class RedisRepository implements CacheRepository {

    private final RedisTemplate<String, UrlCacheDto> redisTemplate;

    private final RedisTemplate<String, String> stringRedisTemplate;

    @Override
    public void save(String shortCode, UrlCacheDto value, Duration ttl) {
        redisTemplate.opsForValue().set(SHORT_KEY_PREFIX + shortCode, value, ttl);
        stringRedisTemplate.opsForValue()
                .set(LONG_KEY_PREFIX + value.getLongUrl(), shortCode, ttl);
    }

    @Override
    public UrlCacheDto get(String shortCode) {
        return redisTemplate.opsForValue().get(SHORT_KEY_PREFIX + shortCode);
    }

    public String getByValue(String longUrl) {
        return stringRedisTemplate.opsForValue().get(LONG_KEY_PREFIX + longUrl);
    }

    @Override
    public boolean contains(String key) {
        return redisTemplate.hasKey(SHORT_KEY_PREFIX + key);
    }
}
