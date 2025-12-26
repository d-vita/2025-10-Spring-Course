package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.CommentDto;


import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({
        CommentServiceImpl.class,
        CommentConverter.class,
        BookConverter.class,
        AuthorConverter.class,
        GenreConverter.class
})
class CommentServiceImplTest {

    private static final String EXISTING_COMMENT_ID = "1";

    private static final String EXISTING_BOOK_ID = "1";

    @Autowired
    private CommentService commentService;


    @Test
    void shouldReturnCorrectCommentById() {
        var actual = commentService.findById(EXISTING_COMMENT_ID);

        assertThat(actual).isPresent();

        var dto = actual.get();
        assertThat(dto.id()).isEqualTo("1");
        assertThat(dto.message()).isEqualTo("Comment_1");

        assertThat(dto.book()).isNotNull();
        assertThat(dto.book().id()).isEqualTo("1");
        assertThat(dto.book().title()).isEqualTo("BookTitle_1");
        assertThat(dto.book().author().fullName()).isEqualTo("Author_1");
        assertThat(dto.book().genre().name()).isEqualTo("Genre_1");
    }


    @Test
    void shouldReturnCommentsByBookId() {
        var actual = commentService.findByBookId(EXISTING_BOOK_ID);

        assertThat(actual)
                .hasSize(2)
                .extracting(CommentDto::message)
                .containsExactlyInAnyOrder("Comment_1", "Comment_3");
    }


    @Test
    @DirtiesContext
    void shouldSaveNewComment() {
        var before = commentService.findByBookId(EXISTING_BOOK_ID);
        assertThat(before).hasSize(2);

        var created = commentService.insert("New comment", EXISTING_BOOK_ID);

        assertThat(created.id()).isNotNull();
        assertThat(created.message()).isEqualTo("New comment");
        assertThat(created.book().id()).isEqualTo(EXISTING_BOOK_ID);

        var after = commentService.findByBookId(EXISTING_BOOK_ID);
        assertThat(after).hasSize(3);
    }


    @Test
    @DirtiesContext
    void shouldUpdateComment() {
        var updated = commentService.update("1", "Updated comment", EXISTING_BOOK_ID);

        assertThat(updated.id()).isEqualTo("1");
        assertThat(updated.message()).isEqualTo("Updated comment");

        var fromDb = commentService.findById("1");
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().message()).isEqualTo("Updated comment");
    }


    @Test
    @DirtiesContext
    void shouldDeleteComment() {
        assertThat(commentService.findByBookId(EXISTING_BOOK_ID)).hasSize(2);

        commentService.deleteById("1");

        assertThat(commentService.findByBookId(EXISTING_BOOK_ID)).hasSize(1);
        assertThat(commentService.findById("1")).isEmpty();
    }

}