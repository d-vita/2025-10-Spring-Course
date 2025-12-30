package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public record BookFormDto(
        Long id,
        @NotBlank(message = "Title cannot be empty")
        @Size(min = 2, max = 100, message = "Title should be between 2 and 100 characters")
        String title,
        @Positive(message = "Author must be selected")
        Long authorId,
        @Positive(message = "Genre must be selected")
        Long genreId
) {
        public static BookFormDto empty() {
                return new BookFormDto(null, null, null, null);
        }
}
