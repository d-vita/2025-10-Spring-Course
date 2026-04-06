package com.urlshortener.service;

import com.urlshortener.dto.UrlCacheDto;
import com.urlshortener.exception.UrlNotFoundException;
import com.urlshortener.kafka.producer.ClickEventProducer;
import com.urlshortener.kafka.producer.UrlCreatedEventProducer;
import com.urlshortener.model.Url;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.repository.cache.CacheRepository;
import com.urlshortener.service.hashgenerator.HashGenerator;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

import static com.urlshortener.constants.Constants.DOMAIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service for generate short URL")
class UrlServiceImplTest {

    private UrlServiceImpl urlService;

    @Mock
    private HashGenerator hashGenerator;

    @Mock
    private CacheRepository cacheRepository;

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private ClickEventProducer clickEventProducer;

    @Mock
    private UrlCreatedEventProducer urlCreatedEventProducer;

    @Mock
    private RateLimiter createUrlRateLimiter;

    @Mock
    private RateLimiter getUrlRateLimiter;

    @BeforeEach
    void setUp() {
        urlService = new UrlServiceImpl(
                hashGenerator,
                cacheRepository,
                urlRepository,
                clickEventProducer,
                urlCreatedEventProducer,
                createUrlRateLimiter,
                getUrlRateLimiter
        );
    }

    @Test
    @DisplayName("Should shorten URL and retrieve it")
    void testShortenAndRetrieve() {
        String longUrl = "https://example.com/test";
        String shortCode = "abc123";
        Long userId = 1L;
        Url savedUrl = new Url("1", shortCode, longUrl, userId, Instant.now());

        when(createUrlRateLimiter.executeSupplier(any())).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
        when(getUrlRateLimiter.executeSupplier(any())).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        when(cacheRepository.getByValue(longUrl)).thenReturn(null);
        when(urlRepository.findByLongUrl(longUrl)).thenReturn(null);
        when(hashGenerator.encode(longUrl)).thenReturn(shortCode);
        when(urlRepository.save(any(Url.class))).thenReturn(savedUrl);

        String shortUrl = urlService.shorten(longUrl, userId);

        assertNotNull(shortUrl);
        assertTrue(shortUrl.startsWith(DOMAIN));
        assertTrue(shortUrl.contains(shortCode));

        UrlCacheDto cachedDto = new UrlCacheDto(longUrl, userId);
        when(cacheRepository.get(shortCode)).thenReturn(cachedDto);

        Optional<String> retrieved = urlService.getOriginalUrl(shortCode);

        assertTrue(retrieved.isPresent());
        assertEquals(longUrl, retrieved.get());
    }

    @Test
    @DisplayName("Should return same short URL for same long URL")
    void testShortenSameUrlReturnsSameShortUrl() {
        String longUrl = "https://example.com/test";
        String shortCode = "abc123";
        Long userId = 1L;

        when(createUrlRateLimiter.executeSupplier(any())).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        when(cacheRepository.getByValue(longUrl)).thenReturn(null);
        when(urlRepository.findByLongUrl(longUrl)).thenReturn(null);
        when(hashGenerator.encode(longUrl)).thenReturn(shortCode);
        when(urlRepository.save(any(Url.class))).thenReturn(
                new Url("1", shortCode, longUrl, userId, Instant.now())
        );

        String shortUrl1 = urlService.shorten(longUrl, userId);

        when(cacheRepository.getByValue(longUrl)).thenReturn(shortCode);

        String shortUrl2 = urlService.shorten(longUrl, userId);

        assertNotNull(shortUrl1);
        assertNotNull(shortUrl2);
        assertEquals(shortUrl1, shortUrl2);
    }

    @Test
    @DisplayName("Should return empty optional for non-existing short URL")
    void testRetrieveNonExistingShortUrl() {
        String shortCode = "nonexistent";

        when(getUrlRateLimiter.executeSupplier(any())).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
        when(cacheRepository.get(shortCode)).thenReturn(null);
        when(urlRepository.findByShortUrl(shortCode)).thenReturn(null);

        assertThrows(UrlNotFoundException.class, () -> urlService.getOriginalUrl(shortCode));
    }

    @Test
    @DisplayName("Should throw exception when userId is null")
    void testShortenWithNullUserIdThrowsException() {
        String longUrl = "https://example.com/test";

        when(createUrlRateLimiter.executeSupplier(any())).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        assertThrows(IllegalArgumentException.class, () -> urlService.shorten(longUrl, null));
    }

    @Test
    @DisplayName("Should use cached URL when available")
    void testRetrieveFromCache() {
        String shortCode = "abc123";
        String longUrl = "https://example.com/test";
        Long userId = 1L;
        UrlCacheDto cachedDto = new UrlCacheDto(longUrl, userId);

        when(getUrlRateLimiter.executeSupplier(any())).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
        when(cacheRepository.get(shortCode)).thenReturn(cachedDto);

        Optional<String> retrieved = urlService.getOriginalUrl(shortCode);

        assertTrue(retrieved.isPresent());
        assertEquals(longUrl, retrieved.get());
    }

    @Test
    @DisplayName("Should fallback to database when cache miss")
    void testRetrieveFromDatabaseOnCacheMiss() {
        String shortCode = "abc123";
        String longUrl = "https://example.com/test";
        Long userId = 1L;
        Url urlFromDb = new Url("1", shortCode, longUrl, userId, Instant.now());

        when(getUrlRateLimiter.executeSupplier(any())).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
        when(cacheRepository.get(shortCode)).thenReturn(null);
        when(urlRepository.findByShortUrl(shortCode)).thenReturn(urlFromDb);

        Optional<String> retrieved = urlService.getOriginalUrl(shortCode);

        assertTrue(retrieved.isPresent());
        assertEquals(longUrl, retrieved.get());
    }
}
