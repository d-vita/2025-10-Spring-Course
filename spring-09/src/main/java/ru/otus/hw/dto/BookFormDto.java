package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record BookFormDto(
        Long id,
        @NotBlank(message = "Title cannot be empty")
        String title,
        @Positive(message = "Author must be selected")
        Long authorId,
        @Positive(message = "Genre must be selected")
        Long genreId
) {
}
