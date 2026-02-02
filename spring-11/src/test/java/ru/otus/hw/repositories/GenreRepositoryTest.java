package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Genre;


@DataMongoTest
class GenreRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_GENRES = 3;
    private static final String NON_EXISTING_GENRE_ID = "999";

    @Autowired
    private GenreRepository repository;

    @Test
    void shouldFindAllGenres() {
        StepVerifier.create(repository.findAll())
                .expectNextCount(EXPECTED_NUMBER_OF_GENRES)
                .verifyComplete();
    }

    @Test
    void shouldFindGenreById() {
        var expectedGenre = new Genre("1", "Genre_1");

        StepVerifier.create(repository.findById("1"))
                .expectNext(expectedGenre)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        StepVerifier.create(repository.findById(NON_EXISTING_GENRE_ID))
                .expectNextCount(0)
                .verifyComplete();
    }
}