package com.urlshortener.service;

import com.urlshortener.dto.UrlInfoDto;
import com.urlshortener.model.Url;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.repository.cache.CacheRepository;
import com.urlshortener.service.hashgenerator.HashGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.urlshortener.constants.Constants.DOMAIN;
import static com.urlshortener.constants.Constants.TTL;


@Slf4j
@Service
@AllArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final HashGenerator hashGenerator;

    private final CacheRepository<String, String> cacheRepository;

    private final UrlRepository urlRepository;

    private final ClickEventProducer clickEventProducer;

    /**
     * Shortens a long URL.
     */
    @Override
    public String shorten(String originalUrl, Long userId) {
        String cachedShortUrl = cacheRepository.getByValue(originalUrl);
        if (cachedShortUrl != null) {
            return DOMAIN + cachedShortUrl;
        }

        String shortCode = hashGenerator.encode(originalUrl);

        Url url = new Url();
        url.setShortUrl(shortCode);
        url.setLongUrl(originalUrl);
        url.setUserId(userId);
        url.setCreatedAt(Instant.now());
        urlRepository.save(url);

        cacheRepository.save(shortCode, originalUrl, TTL);

        return DOMAIN + shortCode;
    }

    /**
     * Retrieves the original URL from the shortened one.
     */
    @Override
    public Optional<String> getOriginalUrl(String shortUrl) {
        String originalUrl = cacheRepository.get(shortUrl);
        Url url;

        if (originalUrl == null) {
            url = urlRepository.findByShortUrl(shortUrl);

            if (url == null) {
                return Optional.empty();
            }

            originalUrl = url.getLongUrl();
            cacheRepository.save(shortUrl, originalUrl, TTL);

        } else {
            // ⚠️ важно: нужен userId → идём в Mongo
            url = urlRepository.findByShortUrl(shortUrl);

            if (url == null) {
                return Optional.of(originalUrl); // fallback
            }
        }

        // 🔥 Kafka event — ВСЕГДА при клике
        try {
            clickEventProducer.sendClickEvent(
                    shortUrl,
                    originalUrl,
                    url.getUserId()
            );
        } catch (Exception e) {
            log.error("Failed to send click event", e);
        }

        return Optional.of(originalUrl);
    }

    /**
     * Retrieves all URLs created by a specific user.
     */
    @Override
    public List<UrlInfoDto> getUserUrls(Long userId) {
        List<Url> urls = urlRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return urls.stream()
                .map(url -> new UrlInfoDto(
                        DOMAIN + url.getShortUrl(),
                        url.getLongUrl(),
                        url.getUserId(),
                        url.getCreatedAt()
                ))
                .toList();
    }
}
