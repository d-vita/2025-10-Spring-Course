package ru.otus.hw.dto;


public record UserDto(long id, String username, String email, String password, TariffDto tariff) {
}
