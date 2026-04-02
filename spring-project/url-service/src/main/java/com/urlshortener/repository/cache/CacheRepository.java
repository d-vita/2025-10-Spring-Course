package com.urlshortener.repository.cache;

import java.time.Duration;

public interface CacheRepository<K, V> {
    void save(K key, V value, Duration ttl);
    V get(K key);
    K getByValue(V value);
    boolean contains(K key);
}
