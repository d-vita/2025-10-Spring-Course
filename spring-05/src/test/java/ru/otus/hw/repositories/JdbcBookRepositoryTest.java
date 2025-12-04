package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@JdbcTest
@Import({JdbcBookRepository.class})
class JdbcBookRepositoryTest {
    @Autowired
    private JdbcBookRepository repository;

    @Test
    void shouldFindAllBooks() {
        var actual = repository.findAll();
        assertThat(actual).isEqualTo(List.of(
                new Book(1L, "BookTitle_1",
                        new Author(1L, "Author_1"),
                        new Genre(1L, "Genre_1")),
                new Book(2L, "BookTitle_2",
                        new Author(2L, "Author_2"),
                        new Genre(2L, "Genre_2")),
                new Book(3L, "BookTitle_3",
                        new Author(3L, "Author_3"),
                        new Genre(3L, "Genre_3"))
        ));
    }

    @Test
    void shouldFindBookById() {
        var actual = repository.findById(3L);
        assertThat(actual).contains(
                new Book(3L, "BookTitle_3",
                        new Author(3L, "Author_3"),
                        new Genre(3L, "Genre_3"))
        );
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    void shouldInsertBook() {
        var book = new Book(
                0L, "InsertedBook",
                new Author(1L, "Author_1"),
                new Genre(1L, "Genre_1")
        );

        var saved = repository.save(book);

        assertThat(saved.getId()).isGreaterThan(3L);

        var actual = repository.findById(saved.getId());
        assertThat(actual).contains(saved);
    }

    @Test
    void shouldUpdateBook() {
        var updated = new Book(
                1L, "UpdatedTitle",
                new Author(2L, "Author_2"),
                new Genre(2L, "Genre_2")
        );

        repository.save(updated);

        var actual = repository.findById(1L);
        assertThat(actual).contains(updated);
    }

    @Test
    void shouldDeleteBookById() {
        repository.deleteById(2L);
        assertThat(repository.findById(2L)).isEmpty();
    }

    @Test
    void shouldThrowWhenDeletingNotExisting() {
        assertThatThrownBy(() -> repository.deleteById(500L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}