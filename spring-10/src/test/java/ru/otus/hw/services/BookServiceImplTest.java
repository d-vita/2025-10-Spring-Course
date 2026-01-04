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
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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

        assertThat(actual).isEqualTo(expected);
    }


    @Test
    @DirtiesContext
    void shouldSaveNewBook() {
        BookFormDto bookForm = new BookFormDto(
                "New Book",
                1L,
                1L
        );

        assertThat(bookService.findAll()).hasSize(3);

        BookDto savedBook = bookService.insert(bookForm);
        assertThat(bookService.findAll()).hasSize(4);

        assertThat(savedBook.title()).isEqualTo("New Book");
        assertThat(savedBook.author())
                .isEqualTo(new AuthorDto(1L, "Author_1"));
        assertThat(savedBook.genre())
                .isEqualTo(new GenreDto(1L, "Genre_1"));

        BookDto foundBook = bookService.findById(savedBook.id());
        assertThat(foundBook).isEqualTo(savedBook);
    }


    @Test
    @DirtiesContext
    void shouldUpdateBook() {
        BookFormDto form = new BookFormDto(
                "Updated Title",
                1L,
                1L
        );

        assertThat(bookService.findAll()).hasSize(3);

        BookDto updatedBook = bookService.update(EXISTING_BOOK_ID, form);

        assertThat(bookService.findAll()).hasSize(3);

        assertThat(updatedBook.id()).isEqualTo(EXISTING_BOOK_ID);
        assertThat(updatedBook.title()).isEqualTo("Updated Title");
        assertThat(updatedBook.author())
                .isEqualTo(new AuthorDto(1L, "Author_1"));
        assertThat(updatedBook.genre())
                .isEqualTo(new GenreDto(1L, "Genre_1"));

        BookDto actualBook = bookService.findById(EXISTING_BOOK_ID);
        assertThat(actualBook).isEqualTo(updatedBook);
    }


    @Test
    @DirtiesContext
    void shouldDeleteBook() {
        assertThat(bookService.findAll()).hasSize(3);

        bookService.deleteById(EXISTING_BOOK_ID);
        assertThat(bookService.findAll()).hasSize(2);
        assertThatThrownBy(() -> bookService.findById(EXISTING_BOOK_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Book");
    }


    private List<BookDto> getExpectedBooks() {
        return List.of(
                new BookDto(1L, "BookTitle_1", new AuthorDto(1L, "Author_1"), new GenreDto(1L, "Genre_1")),
                new BookDto(2L, "BookTitle_2", new AuthorDto(2L, "Author_2"), new GenreDto(2L, "Genre_2")),
                new BookDto(3L, "BookTitle_3", new AuthorDto(3L, "Author_3"), new GenreDto(3L, "Genre_3"))
        );
    }
}