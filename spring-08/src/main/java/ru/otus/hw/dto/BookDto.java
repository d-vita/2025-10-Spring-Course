package ru.otus.hw.dto;


public record BookDto(String id, String title, String authorId, String genreId) {
    @Override
    public String toString() {
        return "Id: %s, title: %s, author: {%s}, genres: %s".formatted(id, title, authorId, genreId);
    }
}
