package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record LoginRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 100, message = "Username should be between 2 and 100 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password should be between 6 and 100 characters")
        String password
) {
}
