package com.urlshortener.controller;

import com.urlshortener.dto.UrlInfoDto;
import com.urlshortener.exception.UrlNotFoundException;
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


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/urls")
public class UrlShortenerController {

    private final UrlService urlService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(
            @RequestParam String originalUrl,
            @RequestParam Long userId
    ) {
        if (UrlValidator.isNotValidUrl(originalUrl)) {
            throw new IllegalArgumentException("Invalid URL: " + originalUrl);
        }

        return urlService.shorten(originalUrl, userId);
    }

    @GetMapping("/{shortCode}")
    public String getUrl(@PathVariable String shortCode) {
        return urlService.getOriginalUrl(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortCode));
    }

    @GetMapping("/user/{userId}")
    public List<UrlInfoDto> getUserUrls(@PathVariable Long userId) {
        return urlService.getUserUrls(userId);
    }
}
