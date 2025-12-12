package ru.otus.hw.dto;

public record GenreDto(long id, String name) {
    @Override
    public String toString() {
        return "Id: %d, Name: %s".formatted(id, name);
    }
}
