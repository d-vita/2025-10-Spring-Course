package ru.otus.hw.repositories;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcAuthorRepository.class)
class JdbcAuthorRepositoryTest {

    @Autowired
    private JdbcAuthorRepository repository;

    @Test
    void shouldFindAllAuthors() {
        var actual = repository.findAll();
        assertThat(actual).isEqualTo(List.of(
                new Author(1L, "Author_1"),
                new Author(2L, "Author_2"),
                new Author(3L, "Author_3")
        ));
    }

    @Test
    void shouldFindAuthorById() {
        var actual = repository.findById(1L);
        assertThat(actual).contains(new Author(1L, "Author_1"));
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(999L)).isEmpty();
    }
}