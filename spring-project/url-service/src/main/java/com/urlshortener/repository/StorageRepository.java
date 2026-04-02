package com.urlshortener.repository;

import com.urlshortener.model.UrlDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StorageRepository extends MongoRepository<UrlDocument, String> {
    UrlDocument findUrlDocumentByShortUrl(String shortUrl);
}
