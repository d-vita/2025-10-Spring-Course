package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest
class GenreRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_GENRES = 3;
    private static final String FIRST_GENRE_ID = "1";
    private static final String NON_EXISTING_GENRE_ID = "999";

    @Autowired
    private GenreRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

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
                        new Genre("1", "Genre_1"),
                        new Genre("2", "Genre_2"),
                        new Genre("3", "Genre_3")
        ));
    }

    @Test
    void shouldFindGenreById() {
        var actualGenre = repository.findById(FIRST_GENRE_ID);
        var expectedGenre = mongoTemplate.findById(FIRST_GENRE_ID, Genre.class);

        assertThat(actualGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(NON_EXISTING_GENRE_ID)).isEmpty();
    }
}