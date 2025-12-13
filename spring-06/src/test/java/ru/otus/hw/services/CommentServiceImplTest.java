package ru.otus.hw.services;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        CommentServiceImpl.class,
        JpaCommentRepository.class,
        JpaBookRepository.class,
        CommentConverter.class
})
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {

    private static final Long EXISTING_COMMENT_ID = 1L;

    private static final Long EXISTING_BOOK_ID = 1L;

    @Autowired
    private CommentService commentService;


    @Test
    void shouldReturnCorrectCommentById() {
        var expected = new CommentDto(EXISTING_COMMENT_ID, "Comment_1", 1L);
        var actual = commentService.findById(EXISTING_COMMENT_ID);

        assertThat(actual).isPresent()
                .hasValue(expected);
    }


    @Test
    void shouldReturnCommentsByBookId() {
        var expected = List.of(
                new CommentDto(1L, "Comment_1", 1L),
                new CommentDto(4L, "Comment_4", 1L)
        );
        var actual = commentService.findAllByBookId(EXISTING_BOOK_ID);

        assertThat(actual).isEqualTo(expected);
    }


    @Test
    @DirtiesContext
    void shouldSaveNewComment() {
        var expected = new CommentDto(5L, "New comment was created", 1L);

        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(2);

        commentService.insert("New comment was created", EXISTING_BOOK_ID);
        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(3);

        var actual = commentService.findById(5L);
        assertThat(actual).isPresent()
                .hasValue(expected);
    }


    @Test
    @DirtiesContext
    void shouldUpdateComment() {
        var expected = new CommentDto(EXISTING_COMMENT_ID, "Test comment was updated", 1L);

        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(2);

        commentService.update(EXISTING_COMMENT_ID, "Test comment was updated", EXISTING_BOOK_ID);
        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(2);

        var actual = commentService.findById(EXISTING_COMMENT_ID);
        assertThat(actual).isPresent()
                .hasValue(expected);
    }


    @Test
    @DirtiesContext
    void shouldDeleteComment() {
        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(2);

        commentService.deleteById(EXISTING_COMMENT_ID);
        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(1);
        assertThat(commentService.findById(EXISTING_COMMENT_ID)).isEmpty();
    }

}