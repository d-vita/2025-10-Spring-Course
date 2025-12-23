package ru.otus.hw.dto;


public record BookDto(String id, String title, AuthorDto author, GenreDto genre) {
    @Override
    public String toString() {
        return "Id: %s, title: %s, author: {%s}, genres: %s".formatted(id, title, author, genre);
    }
}
