package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class CommentRepositoryTest {

    private static final String EXISTING_COMMENT_ID_1 = "1";

    private static final String EXISTING_COMMENT_ID_3 = "3";

    private static final String BOOK_ID = "1";

    private static final String NONEXISTENT_COMMENT_ID = "101";

    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    void shouldReturnCorrectCommentById() {
        var expectedComment = mongoTemplate.findById(EXISTING_COMMENT_ID_1, Comment.class);
        var actualComment = repository.findById(EXISTING_COMMENT_ID_1);

        assertThat(actualComment).isPresent()
                .hasValue(expectedComment);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(NONEXISTENT_COMMENT_ID)).isNotPresent();
    }
    @Test
    void shouldReturnCommentsByBookId() {
        var commentOne = mongoTemplate.findById(EXISTING_COMMENT_ID_1, Comment.class);
        var commentTwo = mongoTemplate.findById(EXISTING_COMMENT_ID_3, Comment.class);

        var actual = repository.findByBookId(BOOK_ID);

        assertThat(actual).containsExactly(commentOne, commentTwo);
    }


    @Test
    @DirtiesContext
    void shouldSaveNewComment() {
        var relatedBook = mongoTemplate.findById(BOOK_ID, Book.class);
        var commentToSave = new Comment(null, "NEW_TEST_COMMENT", relatedBook.getId());

        var savedComment = repository.save(commentToSave);
        assertThat(savedComment).isNotNull()
                .matches(b -> b.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(commentToSave);

        var foundComment = mongoTemplate.findById(savedComment.getId(), Comment.class);
        assertThat(foundComment).isEqualTo(savedComment);
    }


    @Test
    @DirtiesContext
    void shouldUpdateComment() {
        var updatedMessage = "UPDATED_COMMENT_MESSAGE";
        var existingComment = mongoTemplate.findById(EXISTING_COMMENT_ID_1, Comment.class);

        var commentToUpdate = new Comment(existingComment.getId(), updatedMessage, existingComment.getBookId());
        var updated = repository.save(commentToUpdate);

        assertThat(updated).isNotNull()
                .matches(b -> b.getMessage().equals(updatedMessage));

        assertThat(mongoTemplate.findById(updated.getId(), Comment.class)).isEqualTo(updated);
    }


    @Test
    @DirtiesContext
    void shouldDeleteComment() {
        var commentToDelete = mongoTemplate.findById(EXISTING_COMMENT_ID_1, Comment.class);
        assertThat(commentToDelete).isNotNull();

        repository.deleteById(EXISTING_COMMENT_ID_1);
        assertThat(mongoTemplate.findById(EXISTING_COMMENT_ID_1, Comment.class)).isNull();
    }
}