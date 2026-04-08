package ru.otus.hw.dto;

import java.time.Instant;

public record NotificationDto(
        Long id,
        Long userId,
        String shortUrl,
        String message,
        boolean read,
        Instant createdAt
) {
}
