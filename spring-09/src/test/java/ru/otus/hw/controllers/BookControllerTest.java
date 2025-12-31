package ru.otus.hw.controllers;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;
import ru.otus.hw.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private BookConverter bookConverter;

    private BookDto sampleBook;

    @BeforeEach
    void setUp() {
        AuthorDto author = new AuthorDto(1L, "Leo Tolstoy");
        GenreDto genre = new GenreDto(1L, "Novel");

        sampleBook = new BookDto(
                1L,
                "War and Peace",
                author,
                genre
        );

        reset(bookService, authorService, genreService, bookConverter);
    }

    @Test
    void getBooks_ShouldReturnBooksPage() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(sampleBook));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void getAddBookPage_ShouldReturnAddPage() throws Exception {
        when(authorService.findAll()).thenReturn(List.of());
        when(genreService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void saveBook_ShouldRedirect_WhenValid() throws Exception {
        mockMvc.perform(post("/add")
                        .param("title", "New Book")
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService).insert("New Book", 1L, 1L);
    }

    @Test
    void saveBook_ShouldReturnAddPage_WhenValidationFails() throws Exception {
        when(authorService.findAll()).thenReturn(List.of());
        when(genreService.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/add")
                        .param("title", "") // пустое название → ошибка валидации
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("add"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void getEditBookPage_ShouldReturnEditPage() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookConverter.toFormDto(sampleBook)).thenReturn(BookFormDto.empty());
        when(authorService.findAll()).thenReturn(List.of());
        when(genreService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void getEditBookPage_ShouldThrowNotFound_WhenBookNotFound() {
        when(bookService.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                mockMvc.perform(get("/1/edit"))
        )
                .isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateBook_ShouldRedirect_WhenValid() throws Exception {
        mockMvc.perform(post("/1/edit")
                        .param("title", "Updated Book")
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService).update(1L, "Updated Book", 1L, 1L);
    }

    @Test
    void updateBook_ShouldReturnEditPage_WhenValidationFails() throws Exception {
        when(authorService.findAll()).thenReturn(List.of());
        when(genreService.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/1/edit")
                        .param("title", "") // пустое название → ошибка валидации
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void deleteBook_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService).deleteById(1L);
    }
}
