package com.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlInfoDto {

    private String shortUrl;

    private String longUrl;

    private Long userId;

    private Instant createdAt;

}
