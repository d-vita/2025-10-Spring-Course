package ru.otus.hw.dto;


public record CommentDto(String id, String message, String bookId) {
    @Override
    public String toString() {
        return "Id: %s, Message: %s, BookId: %s".formatted(id, message, bookId);
    }
}
