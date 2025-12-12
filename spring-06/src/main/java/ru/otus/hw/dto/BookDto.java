package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;


public record BookDto(long id, String title, AuthorDto author, GenreDto genre) {
    @Override
    public String toString() {
        return "Id: %d, title: %s, author: {%s}, genres: %s".formatted(id, title, author, genre);
    }
}
