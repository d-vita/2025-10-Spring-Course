package ru.otus.hw.repositories;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;


@DataMongoTest
class AuthorRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;
    private static final String NON_EXISTING_AUTHOR_ID = "999";

    @Autowired
    private AuthorRepository repository;

    @Test
    void shouldFindAllAuthors() {
        StepVerifier.create(repository.findAll())
                .expectNextCount(EXPECTED_NUMBER_OF_AUTHORS)
                .verifyComplete();
    }

    @Test
    void shouldFindAuthorById() {
        var expectedAuthor = new Author("1", "Author_1");

        StepVerifier.create(repository.findById("1"))
                .expectNext(expectedAuthor)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        StepVerifier.create(repository.findById(NON_EXISTING_AUTHOR_ID))
                .expectNextCount(0)
                .verifyComplete();
    }
}