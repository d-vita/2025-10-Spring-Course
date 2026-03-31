package ru.otus.hw.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;


public record UserFormDto(
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 2, max = 100, message = "Username should be between 2 and 100 characters")
        String username,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 6, max = 100, message = "Password should be between 6 and 100 characters")
        String password,

        @NotNull(message = "Tariff must be selected")
        @Positive(message = "Tariff must be valid")
        Long tariffId
) {
}
