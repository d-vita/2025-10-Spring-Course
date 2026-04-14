package com.urlshortener.repository.cache;

import com.urlshortener.dto.UrlCacheDto;

import java.time.Duration;

public interface CacheRepository {

    void save(String shortCode, UrlCacheDto dto, Duration ttl);

    UrlCacheDto get(String shortCode);

    String getByValue(String longUrl);

    boolean contains(String shortCode);

    void deleteByShortCode(String shortCode);

    void deleteByLongUrl(String longUrl);
}
