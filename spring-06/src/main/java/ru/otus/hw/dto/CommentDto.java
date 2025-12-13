package ru.otus.hw.dto;


public record CommentDto(Long id, String message, long bookId) {
    @Override
    public String toString() {
        return "Id: %d, Message: %s, BookId: %d".formatted(id, message, bookId);
    }
}
