package com.urlshortener.service;

import com.urlshortener.dto.UrlInfoDto;

import java.util.List;
import java.util.Optional;

public interface UrlService {

    String shorten(String longUrl, Long userId);

    Optional<String> getUrl(String shortUrl);

    List<UrlInfoDto> getUserUrls(Long userId);
}
