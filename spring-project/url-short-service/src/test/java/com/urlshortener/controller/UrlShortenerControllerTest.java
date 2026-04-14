package com.urlshortener.controller;

import com.urlshortener.controller.UrlShortenerController;
import com.urlshortener.dto.UrlInfoDto;
import com.urlshortener.exception.GlobalExceptionHandler;
import com.urlshortener.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlShortenerController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("URL Shortener Controller Tests")
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    @Test
    @DisplayName("Should shorten URL and return short URL")
    void testShortenUrl() throws Exception {
        String longUrl = "https://example.com";
        String shortUrl = "http://localhost:8080/abc123";
        Long userId = 1L;

        when(urlService.shorten(longUrl, userId)).thenReturn(shortUrl);

        mockMvc.perform(post("/api/urls")
                        .param("originalUrl", longUrl)
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(content().string(shortUrl));
    }

    @Test
    @DisplayName("Should retrieve original URL by short code")
    void testRetrieveUrl() throws Exception {
        String shortCode = "abc123";
        String longUrl = "https://example.com";

        when(urlService.getOriginalUrl(shortCode)).thenReturn(Optional.of(longUrl));

        mockMvc.perform(get("/api/urls/{shortCode}", shortCode))
                .andExpect(status().isOk())
                .andExpect(content().string(longUrl));
    }

    @Test
    @DisplayName("Should return 404 when short URL not found")
    void testRetrieveUrlNotFound() throws Exception {
        String shortCode = "unknown";

        when(urlService.getOriginalUrl(shortCode)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/urls/{shortCode}", shortCode))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Short URL not found: unknown"));
    }

    @Test
    @DisplayName("Should return 400 when URL is invalid")
    void testShortenUrlInvalidValues() throws Exception {
        String invalidUrl = "not-a-valid-url";
        Long userId = 1L;

        mockMvc.perform(post("/api/urls")
                        .param("originalUrl", invalidUrl)
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get all URLs for a user")
    void testGetUserUrls() throws Exception {
        Long userId = 1L;
        List<UrlInfoDto> userUrls = List.of(
                new UrlInfoDto("http://localhost:8080/abc123", "https://example.com/1", userId, Instant.now()),
                new UrlInfoDto("http://localhost:8080/def456", "https://example.com/2", userId, Instant.now())
        );

        when(urlService.getUserUrls(userId)).thenReturn(userUrls);

        mockMvc.perform(get("/api/urls/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].shortUrl").value("http://localhost:8080/abc123"))
                .andExpect(jsonPath("$[0].longUrl").value("https://example.com/1"))
                .andExpect(jsonPath("$[1].shortUrl").value("http://localhost:8080/def456"))
                .andExpect(jsonPath("$[1].longUrl").value("https://example.com/2"));
    }

    @Test
    @DisplayName("Should return empty list when user has no URLs")
    void testGetUserUrlsEmpty() throws Exception {
        Long userId = 999L;

        when(urlService.getUserUrls(userId)).thenReturn(List.of());

        mockMvc.perform(get("/api/urls/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should handle rate limit exceeded")
    void testRateLimitExceeded() throws Exception {
        String longUrl = "https://example.com";
        Long userId = 1L;

        when(urlService.shorten(anyString(), anyLong()))
                .thenThrow(new IllegalStateException("Rate limit exceeded. Please try again later."));

        mockMvc.perform(post("/api/urls")
                        .param("originalUrl", longUrl)
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should return correct content type for JSON responses")
    void testContentTypeJson() throws Exception {
        Long userId = 1L;
        when(urlService.getUserUrls(userId)).thenReturn(List.of());

        mockMvc.perform(get("/api/urls/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}