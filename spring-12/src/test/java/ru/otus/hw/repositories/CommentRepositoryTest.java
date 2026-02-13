package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    private static final Long EXISTING_COMMENT_ID_1 = 1L;

    private static final Long EXISTING_COMMENT_ID_4 = 4L;

    private static final Long BOOK_ID = 1L;

    private static final Long NONEXISTENT_COMMENT_ID = 101L;

    @Autowired
    private CommentRepository repository;

    @Autowired
    private TestEntityManager em;


    @Test
    void shouldReturnCorrectCommentById() {
        var expectedComment = em.find(Comment.class, EXISTING_COMMENT_ID_1);
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
        var commentOne = em.find(Comment.class, EXISTING_COMMENT_ID_1);
        var commentTwo = em.find(Comment.class, EXISTING_COMMENT_ID_4);

        var actual = repository.findAllByBookId(BOOK_ID);

        assertThat(actual).containsExactly(commentOne, commentTwo);
    }


    @Test
    @DirtiesContext
    void shouldSaveNewComment() {
        var relatedBook = em.find(Book.class, BOOK_ID);
        var commentToSave = new Comment(0, "NEW_TEST_COMMENT", relatedBook);

        var savedComment = repository.save(commentToSave);
        assertThat(savedComment).isNotNull()
                .matches(b -> b.getId() != 0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(commentToSave);

        var foundComment = em.find(Comment.class, savedComment.getId());
        assertThat(foundComment).isEqualTo(savedComment);
    }


    @Test
    @DirtiesContext
    void shouldUpdateComment() {
        var updatedMessage = "UPDATED_COMMENT_MESSAGE";
        var existingComment = em.find(Comment.class, EXISTING_COMMENT_ID_1);

        var commentToUpdate = new Comment(existingComment.getId(), updatedMessage, existingComment.getBook());
        var updated = repository.save(commentToUpdate);

        assertThat(updated).isNotNull()
                .matches(b -> b.getMessage().equals(updatedMessage));

        assertThat(em.find(Comment.class, updated.getId())).isEqualTo(updated);
    }


    @Test
    @DirtiesContext
    void shouldDeleteComment() {
        var commentToDelete = em.find(Comment.class, EXISTING_COMMENT_ID_1);
        assertThat(commentToDelete).isNotNull();

        repository.deleteById(EXISTING_COMMENT_ID_1);
        assertThat(em.find(Comment.class, EXISTING_COMMENT_ID_1)).isNull();
    }
}