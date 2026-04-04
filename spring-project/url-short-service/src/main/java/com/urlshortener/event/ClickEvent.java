package com.urlshortener.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Event published when e new URL is CLICKED.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClickEvent {

    private String shortUrl;

    private String longUrl;

    private Long userId;

    private Instant clickedAt;
}
