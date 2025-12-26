package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({
        BookServiceImpl.class,
        BookConverter.class,
        AuthorConverter.class,
        GenreConverter.class
})
class BookServiceImplTest {

    private static final String EXISTING_BOOK_ID = "1";

    @Autowired
    private BookService bookService;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookService.findAll();
        var expectedBooks = getExpectedBooks();

        assertThat(actualBooks).isEqualTo(expectedBooks);
    }


    @Test
    void shouldReturnCorrectBookById() {
        var actual = bookService.findById("1");
        var expected = getExpectedBooks().get(0);

        assertThat(actual)
                .isPresent()
                .hasValue(expected);
    }


    @Test
    @DirtiesContext
    void shouldSaveNewBook() {
        BookDto expectedBook = new BookDto(
                null,
                "New Book",
                new AuthorDto("1", "Author_1"),
                new GenreDto("1", "Genre_1")
        );

        assertThat(bookService.findAll()).hasSize(3);

        BookDto actual = bookService.insert("New Book", "1", "1");

        assertThat(bookService.findAll()).hasSize(4);

        assertThat(actual.id())
                .isNotNull()
                .isNotEmpty()
                .isNotBlank();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBook);
    }


    @Test
    @DirtiesContext
    void shouldUpdateBook() {
        var expected = new BookDto(EXISTING_BOOK_ID, "Updated Title", new AuthorDto("1", "Author_1"), new GenreDto("1", "Genre_1"));

        assertThat(bookService.findAll()).hasSize(3);

        bookService.update(EXISTING_BOOK_ID, "Updated Title", "1", "1");
        assertThat(bookService.findAll()).hasSize(3);


        var actualBook = bookService.findById(EXISTING_BOOK_ID);

        assertThat(actualBook).isPresent()
                .hasValue(expected);
    }


    @Test
    @DirtiesContext
    void shouldDeleteBook() {
        assertThat(bookService.findAll()).hasSize(3);

        bookService.deleteById(EXISTING_BOOK_ID);
        assertThat(bookService.findAll()).hasSize(2);
        assertThat(bookService.findById(EXISTING_BOOK_ID)).isEmpty();
    }

    @Test
    @DirtiesContext
    void shouldDeleteCommentAfterDeleteBook() {
        var commentsBefore = commentRepository.findByBookId("1");
        assertThat(commentsBefore).isNotEmpty().hasSize(2);

        bookService.deleteById("1");

        var commentsAfter = commentRepository.findByBookId("1");
        assertThat(commentsAfter).isEmpty();
    }


    private List<BookDto> getExpectedBooks() {
        return List.of(
                new BookDto("1", "BookTitle_1", new AuthorDto("1", "Author_1"), new GenreDto("1", "Genre_1")),
                new BookDto("2", "BookTitle_2", new AuthorDto("2", "Author_2"), new GenreDto("2", "Genre_2")),
                new BookDto("3", "BookTitle_3", new AuthorDto("3", "Author_3"), new GenreDto("3", "Genre_3"))
        );
    }
}