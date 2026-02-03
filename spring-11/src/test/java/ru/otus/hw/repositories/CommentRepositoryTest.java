package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Comment;


@DataMongoTest
class CommentRepositoryTest {

    private static final String NONEXISTENT_COMMENT_ID = "101";

    @Autowired
    private CommentRepository repository;


    @Test
    void shouldReturnCommentsById() {
        StepVerifier.create(repository.findAllByBookId("1"))
                .expectNext(
                        new Comment("1", "Comment1", "1"),
                        new Comment("3", "Comment3", "1")
                )
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        StepVerifier.create(repository.findAllByBookId(NONEXISTENT_COMMENT_ID))
                .expectNextCount(0)
                .verifyComplete();
    }
}