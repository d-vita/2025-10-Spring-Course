package ru.otus.hw.dto;


public record UserDto(
        String username,
        String email,
        TariffDto tariff
) {
}
