package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(JpaGenreRepository.class)
class JpaGenreRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_GENRES = 3;
    private static final long FIRST_GENRE_ID = 2L;
    private static final long NON_EXISTING_GENRE_ID = 999L;

    @Autowired
    private JpaGenreRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldFindAllGenres() {
        var genres = repository.findAll();

        assertThat(genres)
                .isNotNull()
                .hasSize(EXPECTED_NUMBER_OF_GENRES)
                .allMatch(g -> g.getName() != null && !g.getName().isBlank());

        assertThat(genres)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new Genre(1L, "Genre_1"),
                        new Genre(2L, "Genre_2"),
                        new Genre(3L, "Genre_3")
        ));
    }

    @Test
    void shouldFindGenreById() {
        var actualGenre = repository.findById(FIRST_GENRE_ID);
        var expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);

        assertThat(actualGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(NON_EXISTING_GENRE_ID)).isEmpty();
    }
}