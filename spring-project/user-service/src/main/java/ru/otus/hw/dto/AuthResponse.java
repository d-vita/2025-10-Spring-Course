package ru.otus.hw.dto;

public record AuthResponse(
        String token,
        UserDto user
) {
}
