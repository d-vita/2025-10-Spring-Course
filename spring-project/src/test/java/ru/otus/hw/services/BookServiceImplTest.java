package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.TariffConverter;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@Import({
        UserServiceImpl.class,
        UserConverter.class,
        TariffConverter.class,
        GenreConverter.class
})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    private static final Long EXISTING_BOOK_ID = 1L;

    @Autowired
    private UserService bookService;

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
        UserFormDto bookForm = new UserFormDto(
                "New Book",
                1L,
                1L
        );

        assertThat(bookService.findAll()).hasSize(3);

        UserDto savedBook = bookService.insert(bookForm);
        assertThat(bookService.findAll()).hasSize(4);

        assertThat(savedBook.title()).isEqualTo("New Book");
        assertThat(savedBook.author())
                .isEqualTo(new TariffDto(1L, "Author_1"));
        assertThat(savedBook.genre())
                .isEqualTo(new GenreDto(1L, "Genre_1"));

        UserDto foundBook = bookService.findById(savedBook.id());
        assertThat(foundBook).isEqualTo(savedBook);
    }


    @Test
    @DirtiesContext
    void shouldUpdateBook() {
        UserFormDto form = new UserFormDto(
                "Updated Title",
                1L,
                1L
        );

        assertThat(bookService.findAll()).hasSize(3);

        UserDto updatedBook = bookService.update(EXISTING_BOOK_ID, form);

        assertThat(bookService.findAll()).hasSize(3);

        assertThat(updatedBook.id()).isEqualTo(EXISTING_BOOK_ID);
        assertThat(updatedBook.title()).isEqualTo("Updated Title");
        assertThat(updatedBook.author())
                .isEqualTo(new TariffDto(1L, "Author_1"));
        assertThat(updatedBook.genre())
                .isEqualTo(new GenreDto(1L, "Genre_1"));

        UserDto actualBook = bookService.findById(EXISTING_BOOK_ID);
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


    private List<UserDto> getExpectedBooks() {
        return List.of(
                new UserDto(1L, "BookTitle_1", new TariffDto(1L, "Author_1"), new GenreDto(1L, "Genre_1")),
                new UserDto(2L, "BookTitle_2", new TariffDto(2L, "Author_2"), new GenreDto(2L, "Genre_2")),
                new UserDto(3L, "BookTitle_3", new TariffDto(3L, "Author_3"), new GenreDto(3L, "Genre_3"))
        );
    }
}