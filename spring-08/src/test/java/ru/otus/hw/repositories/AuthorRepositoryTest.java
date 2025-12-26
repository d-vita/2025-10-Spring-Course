package ru.otus.hw.repositories;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class AuthorRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;
    private static final String FIRST_AUTHOR_ID = "1";
    private static final String NON_EXISTING_AUTHOR_ID = "999";

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

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
                        new Author("1", "Author_1"),
                        new Author("2", "Author_2"),
                        new Author("3", "Author_3")
                ));
    }

    @Test
    void shouldFindAuthorById() {
        var actualAuthor = repository.findById(FIRST_AUTHOR_ID);
        var expectedAuthor = mongoTemplate.findById(FIRST_AUTHOR_ID, Author.class);

        assertThat(actualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(NON_EXISTING_AUTHOR_ID)).isEmpty();
    }
}