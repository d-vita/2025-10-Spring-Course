package com.urlshortener.service;

import java.util.Optional;

public interface UrlShortener {
    String shorten(String longUrl);
    Optional<String> retrieve(String shortUrl);
}
