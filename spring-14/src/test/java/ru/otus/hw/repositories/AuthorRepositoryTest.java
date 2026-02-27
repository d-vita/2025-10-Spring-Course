package ru.otus.hw.repositories;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.jpa.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;
    private static final long FIRST_AUTHOR_ID = 1L;
    private static final long NON_EXISTING_AUTHOR_ID = 999L;

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldFindAllAuthors() {
        var authors = repository.findAll();

        assertThat(authors)
                .isNotNull()
                .hasSize(EXPECTED_NUMBER_OF_AUTHORS)
                .allMatch(a -> a.getFullName() != null && !a.getFullName().isBlank());

        assertThat(authors)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new Author(1L, "Author_1"),
                        new Author(2L, "Author_2"),
                        new Author(3L, "Author_3")
                ));
    }

    @Test
    void shouldFindAuthorById() {
        var actualAuthor = repository.findById(FIRST_AUTHOR_ID);
        var expectedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);

        assertThat(actualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(NON_EXISTING_AUTHOR_ID)).isEmpty();
    }
}