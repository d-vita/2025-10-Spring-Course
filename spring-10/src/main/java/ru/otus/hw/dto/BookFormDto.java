package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;


public record BookFormDto(
        Long id,
        @NotBlank(message = "Title cannot be empty")
        @Size(min = 2, max = 100, message = "Title should be between 2 and 100 characters")
        String title,

        @NotNull(message = "Author must be selected")
        @Positive(message = "Author must be selected")
        Long authorId,

        @NotNull(message = "Genre must be selected")
        @Positive(message = "Genre must be selected")
        Long genreId
) {
}
