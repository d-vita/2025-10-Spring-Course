package com.urlshortener.controller;

import com.urlshortener.dto.ShortUrl;
import com.urlshortener.dto.UrlInfoDto;
import com.urlshortener.service.UrlService;
import com.urlshortener.validation.UrlValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

import static com.urlshortener.constants.Constants.DOMAIN;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/urls")
public class UrlShortenerController {

    private final UrlService urlService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShortUrl create(
            @RequestParam String originalUrl,
            @RequestParam(required = false) Long userId
    ) {
        if (UrlValidator.isNotValidUrl(originalUrl)) {
            throw new IllegalArgumentException("Invalid URL: " + originalUrl);
        }

        return new ShortUrl(urlService.shorten(originalUrl, userId));
    }

    @GetMapping("/{shortCode}")
    public ShortUrl getUrl(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalUrl(shortCode)
                .orElseThrow(() -> new NoSuchElementException("Short URL not found"));

        return new ShortUrl(originalUrl);
    }

    @GetMapping("/user/{userId}")
    public List<UrlInfoDto> getUserUrls(@PathVariable Long userId) {
        return urlService.getUserUrls(userId);
    }
}
