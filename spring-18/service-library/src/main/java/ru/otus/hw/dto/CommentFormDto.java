package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public record CommentFormDto(
        @NotBlank(message = "Message cannot be empty")
        @Size(min = 2, max = 100, message = "Message should be between 2 and 100 characters")
        String message,

        @NotNull(message = "Book must be selected")
        @Positive(message = "Book must be selected")
        long bookId
) {
}
