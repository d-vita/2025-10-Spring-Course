package ru.otus.hw.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record TariffFormDto(
        @NotNull(message = "Tariff name cannot be null")
        String name,

        @NotNull(message = "Max Links cannot be null")
        @Positive(message = "Max Links should be positive")
        Long maxLinks,

        @NotNull(message = "Max Clicks PerLink cannot be null")
        @Positive(message = " Max Clicks PerLink should be positive")
        Long maxClicksPerLink
) {
}
