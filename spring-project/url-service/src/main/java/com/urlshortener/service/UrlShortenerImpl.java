package com.urlshortener.service;

import com.urlshortener.aspect.Loggable;
import com.urlshortener.model.UrlDocument;
import com.urlshortener.repository.cache.CacheRepository;
import com.urlshortener.repository.StorageRepository;
import com.urlshortener.service.hashgenerator.HashGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

import static com.urlshortener.constants.Constants.*;

@Service
@AllArgsConstructor
public class UrlShortenerImpl implements UrlShortener {
    private final HashGenerator generator;
    private final CacheRepository<String, String> cacheRepository;
    private final StorageRepository storageRepository;

    /**
     * Shortens a long URL.
     * @param originalUrl original URL
     * @return shortened URL with domain
     */
    @Override
    @Loggable
    public String shorten(String originalUrl) {
        //simple protection from double click
        String cachedShortUrl = cacheRepository.getByValue(originalUrl);
        if (cachedShortUrl != null) {
            return DOMAIN + cachedShortUrl;
        }

        String shortCode = generator.encode(originalUrl);

        UrlDocument doc = new UrlDocument();
        doc.setShortUrl(shortCode);
        doc.setLongUrl(originalUrl);
        doc.setCreatedAt(Instant.now());

        storageRepository.save(doc);
        cacheRepository.save(shortCode, originalUrl, TTL);

        return DOMAIN + shortCode;
    }

    /**
     * Retrieves the original URL from the shortened one.
     * @param shortUrl shortened URL
     * @return Optional with the original URL or empty
     */
    @Override
    @Loggable
    public Optional<String> retrieve(String shortUrl) {
        String shortCode = shortUrl.substring(DOMAIN.length());

        String originalUrl = cacheRepository.get(shortCode);

        if (originalUrl == null) {
            UrlDocument doc = storageRepository.findUrlDocumentByShortUrl(shortCode);
            if (doc != null) {
                originalUrl = doc.getLongUrl();
                cacheRepository.save(shortCode, originalUrl, TTL);
            }
        }
        return Optional.ofNullable(originalUrl);
    }
}
