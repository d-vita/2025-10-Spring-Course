package com.urlshortener.repository;

import com.urlshortener.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UrlRepository extends MongoRepository<Url, String> {
    Url findByShortUrl(String shortUrl);

    List<Url> findByUserIdOrderByCreatedAtDesc(Long userId);

    Url findByLongUrl(String originalUrl);
}
