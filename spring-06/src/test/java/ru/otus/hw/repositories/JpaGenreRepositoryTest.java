package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@Import(JpaGenreRepository.class)
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository repository;

    @Test
    void shouldFindAllGenres() {
        var actual = repository.findAll();
        assertThat(actual).isEqualTo(List.of(
                new Genre(1L, "Genre_1"),
                new Genre(2L, "Genre_2"),
                new Genre(3L, "Genre_3")
        ));
    }

    @Test
    void shouldFindGenreById() {
        var actual = repository.findById(2L);
        assertThat(actual).contains(new Genre(2L, "Genre_2"));
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(777L)).isEmpty();
    }
}