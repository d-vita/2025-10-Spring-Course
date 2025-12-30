package ru.otus.hw.dto;


public record BookDto(long id, String title, AuthorDto author, GenreDto genre) {
    @Override
    public String toString() {
        return "Id: %d, title: %s, author: {%s}, genres: %s".formatted(id, title, author, genre);
    }
}
