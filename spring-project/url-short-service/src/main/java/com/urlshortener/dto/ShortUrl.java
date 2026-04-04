package com.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
@AllArgsConstructor
public class ShortUrl {

    private String shortUrl;

    private String longUrl;

    private Long userId;

    private Instant clickedAt;
}
