package ru.otus.hw.dto;


public record CommentDto(String id, String message, BookDto book) {
    @Override
    public String toString() {
        return "Id: %s, Message: %s, Book: %s".formatted(id, message, book);
    }
}
