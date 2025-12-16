package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        BookServiceImpl.class,

        BookConverter.class,
        AuthorConverter.class,
        GenreConverter.class
})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    private static final Long EXISTING_BOOK_ID = 1L;

    @Autowired
    private BookService bookService;

    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookService.findAll();
        var expectedBooks = getExpectedBooks();

        assertThat(actualBooks).isEqualTo(expectedBooks);
    }


    @Test
    void shouldReturnCorrectBookById() {
        var actual = bookService.findById(1L);
        var expected = getExpectedBooks().get(0);

        assertThat(actual)
                .isPresent()
                .hasValue(expected);
    }


    @Test
    @DirtiesContext
    void shouldSaveNewBook() {
        BookDto expectedBook = new BookDto(
                4L,
                "New Book",
                new AuthorDto(1L, "Author_1"),
                new GenreDto(1L, "Genre_1")
        );

        assertThat(bookService.findAll()).hasSize(3);

        bookService.insert("New Book", 1L, 1L);
        assertThat(bookService.findAll()).hasSize(4);

        var actual = bookService.findById(4L);
        assertThat(actual).isPresent()
                .hasValue(expectedBook);
    }


    @Test
    @DirtiesContext
    void shouldUpdateBook() {
        var expected = new BookDto(EXISTING_BOOK_ID, "Updated Title", new AuthorDto(1L, "Author_1"), new GenreDto(1L, "Genre_1"));

        assertThat(bookService.findAll()).hasSize(3);

        bookService.update(EXISTING_BOOK_ID, "Updated Title", 1L, 1L);
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


    private List<BookDto> getExpectedBooks() {
        return List.of(
                new BookDto(1L, "BookTitle_1", new AuthorDto(1L, "Author_1"), new GenreDto(1L, "Genre_1")),
                new BookDto(2L, "BookTitle_2", new AuthorDto(2L, "Author_2"), new GenreDto(2L, "Genre_2")),
                new BookDto(3L, "BookTitle_3", new AuthorDto(3L, "Author_3"), new GenreDto(3L, "Genre_3"))
        );
    }
}