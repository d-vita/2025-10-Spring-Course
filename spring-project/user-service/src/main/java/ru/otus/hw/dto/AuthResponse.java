package ru.otus.hw.dto;

public record AuthResponse(
        Long userId,
        String username,
        String token
) {
}
