package com.urlshortener.controller;

import com.urlshortener.dto.ShortUrl;
import com.urlshortener.service.UrlShortener;
import com.urlshortener.validation.UrlValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.urlshortener.constants.Constants.DOMAIN;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/urls")
public class UrlShortenerController {
    private final UrlShortener urlShortener;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShortUrl create(@RequestParam String originalUrl) {
        if (UrlValidator.isNotValidUrl(originalUrl)) {
            return new ShortUrl("Invalid url:" + originalUrl);
        }

        String shortUrl = urlShortener.shorten(originalUrl);
        return new ShortUrl(shortUrl);
    }

    @GetMapping("/{shortCode}")
    public ShortUrl getUrl(@PathVariable String shortCode) {
        String shortUrl = DOMAIN + shortCode;

        if (!shortUrl.startsWith(DOMAIN)) {
            throw new IllegalArgumentException("Invalid url: " + shortUrl);
        }

        return urlShortener.retrieve(shortUrl)
                .map(ShortUrl::new)
                .orElseThrow(() -> new NoSuchElementException("Short URL not found"));
    }
}
