package com.urlshortener.service;

import com.urlshortener.dto.UrlCacheDto;
import com.urlshortener.dto.UrlInfoDto;
import com.urlshortener.exception.UrlNotFoundException;
import com.urlshortener.model.Url;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.repository.cache.CacheRepository;
import com.urlshortener.service.hashgenerator.HashGenerator;
import com.urlshortener.kafka.producer.ClickEventProducer;
import com.urlshortener.kafka.producer.UrlCreatedEventProducer;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
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

    private final CacheRepository cacheRepository;

    private final UrlRepository urlRepository;

    private final ClickEventProducer clickEventProducer;

    private final UrlCreatedEventProducer urlCreatedEventProducer;

    private final RateLimiter createUrlRateLimiter;

    private final RateLimiter getUrlRateLimiter;

    /**
     * Shortens a long URL.
     */
    @Override
    public String shorten(String originalUrl, Long userId) {
        try {
            return createUrlRateLimiter.executeSupplier(() -> {
                if (userId == null) {
                    throw new IllegalArgumentException("UserId must not be null");
                }

                String cachedShortUrl = cacheRepository.getByValue(originalUrl);
                if (cachedShortUrl != null) {
                    return DOMAIN + cachedShortUrl;
                }

                Url existing = urlRepository.findByLongUrl(originalUrl);
                if (existing != null) {
                    return DOMAIN + existing.getShortUrl();
                }

                String shortCode = hashGenerator.encode(originalUrl);
                Url url = new Url(null, shortCode, originalUrl, userId, Instant.now());
                urlRepository.save(url);
                cacheRepository.save(shortCode, new UrlCacheDto(originalUrl, userId), TTL);

                sendUrlCreatedEvent(shortCode, originalUrl, userId);

                return DOMAIN + shortCode;
            });
        } catch (RequestNotPermitted e) {
            log.warn("Rate limit exceeded for creating URL by userId: {}", userId);
            throw new IllegalStateException("Rate limit exceeded. Please try again later.", e);
        }
    }

    /**
     * Retrieves the original URL from the shortened one.
     */
    @Override
    public Optional<String> getOriginalUrl(String shortUrl) {
        try {
            return getUrlRateLimiter.executeSupplier(() -> {
                UrlCacheDto cached = cacheRepository.get(shortUrl);

                if (cached != null) {
                    sendClickEvent(shortUrl, cached);
                    return Optional.of(cached.getLongUrl());
                }

                Url url = urlRepository.findByShortUrl(shortUrl);

                if (url == null) {
                    throw new UrlNotFoundException("Short URL not found: " + shortUrl);
                }

                UrlCacheDto dto = new UrlCacheDto(url.getLongUrl(), url.getUserId());

                cacheRepository.save(shortUrl, dto, TTL);

                sendClickEvent(shortUrl, dto);

                return Optional.of(dto.getLongUrl());
            });
        } catch (RequestNotPermitted e) {
            log.warn("Rate limit exceeded for getting URL: {}", shortUrl);
            throw new IllegalStateException("Rate limit exceeded for getting URL: {}" + shortUrl);
        }
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

    /**
     * Deletes all URLs created by a specific user.
     * This includes both MongoDB records and Redis cache entries.
     */
    @Override
    public void deleteAllByUserId(Long userId) {
        log.info("Starting deletion of all URLs for userId: {}", userId);

        List<Url> userUrls = urlRepository.findByUserIdOrderByCreatedAtDesc(userId);

        for (Url url : userUrls) {
            try {
                cacheRepository.deleteByShortCode(url.getShortUrl());
                cacheRepository.deleteByLongUrl(url.getLongUrl());
            } catch (Exception e) {
                log.error("Error deleting cache for URL: {}", url.getShortUrl(), e);
            }
        }

        urlRepository.deleteByUserId(userId);

        log.info("Successfully deleted {} URLs for userId: {}", userUrls.size(), userId);
    }

    private void sendClickEvent(String shortUrl, UrlCacheDto dto) {
        try {
            clickEventProducer.sendClickEvent(
                    shortUrl,
                    dto.getLongUrl(),
                    dto.getUserId()
            );
        } catch (Exception e) {
            log.error("Kafka error sending click added event", e);
        }
    }

    private void sendUrlCreatedEvent(String shortUrl, String longUrl, Long userId) {
        try {
            urlCreatedEventProducer.sendUrlCreatedEvent(
                    shortUrl,
                    longUrl,
                    userId
            );
        } catch (Exception e) {
            log.error("Kafka error sending url created event", e);
        }
    }
}
