package com.urlshortener.service;

import com.urlshortener.aspect.Loggable;
import com.urlshortener.dto.UrlInfoDto;
import com.urlshortener.model.Url;
import com.urlshortener.repository.cache.CacheRepository;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.service.hashgenerator.HashGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.urlshortener.constants.Constants.*;

@Service
@AllArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final HashGenerator hashGenerator;

    private final CacheRepository<String, String> cacheRepository;

    private final UrlRepository urlRepository;

    /**
     * Shortens a long URL.
     */
    @Override
    @Loggable
    public String shorten(String originalUrl, Long userId) {
        //simple protection from double click
        String cachedShortUrl = cacheRepository.getByValue(originalUrl);
        if (cachedShortUrl != null) {
            return DOMAIN + cachedShortUrl;
        }

        String shortCode = hashGenerator.encode(originalUrl);

        Url doc = new Url();
        doc.setShortUrl(shortCode);
        doc.setLongUrl(originalUrl);
        doc.setCreatedAt(Instant.now());

        urlRepository.save(doc);
        cacheRepository.save(shortCode, originalUrl, TTL);

        return DOMAIN + shortCode;
    }

    /**
     * Retrieves the original URL from the shortened one.
     */
    @Override
    @Loggable
    public Optional<String> getUrl(String shortUrl) {
        String shortCode = shortUrl.substring(DOMAIN.length());

        String originalUrl = cacheRepository.get(shortCode);

        if (originalUrl == null) {
            Url url = urlRepository.findByShortUrl(shortCode);
            if (url != null) {
                originalUrl = url.getLongUrl();
                cacheRepository.save(shortCode, originalUrl, TTL);
            }
        }
        return Optional.ofNullable(originalUrl);
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
