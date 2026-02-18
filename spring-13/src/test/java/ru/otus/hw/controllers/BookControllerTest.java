package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @WithMockUser(username = "user")
    void shouldAllowAuthorizedAccess() throws Exception {

        Mockito.when(bookService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldReturnAllBooks() throws Exception {
        List<BookDto> books = List.of(
                new BookDto(1L, "Book 1", new AuthorDto(1L, "Author 1"), new GenreDto(1L, "Fiction")),
                new BookDto(2L, "Book 2", new AuthorDto(2L, "Author 2"), new GenreDto(2L, "Science"))
        );
        Mockito.when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(books.size()))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[0].author.fullName").value("Author 1"))
                .andExpect(jsonPath("$[0].genre.name").value("Fiction"));
    }

    @Test
    @WithMockUser
    void shouldReturnBookById() throws Exception {
        BookDto book = new BookDto(1L, "Book 1", new AuthorDto(1L, "Author 1"), new GenreDto(1L, "Fiction"));
        Mockito.when(bookService.findById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book 1"))
                .andExpect(jsonPath("$.author.fullName").value("Author 1"))
                .andExpect(jsonPath("$.genre.name").value("Fiction"));
    }

    @Test
    @WithMockUser
    void shouldCreateBook() throws Exception {
        BookFormDto form = new BookFormDto("New Book", 1L, 1L);
        BookDto created = new BookDto(1L, "New Book", new AuthorDto(1L, "Author 1"), new GenreDto(1L, "Fiction"));
        Mockito.when(bookService.insert(any(BookFormDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Book\",\"authorId\":1,\"genreId\":1}")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author.fullName").value("Author 1"))
                .andExpect(jsonPath("$.genre.name").value("Fiction"));
    }

    @Test
    @WithMockUser
    void shouldUpdateBook() throws Exception {
        BookFormDto form = new BookFormDto("Updated Book", 1L, 1L);
        BookDto updated = new BookDto(1L, "Updated Book", new AuthorDto(1L, "Author 1"), new GenreDto(1L, "Fiction"));

        Mockito.when(bookService.update(eq(1L), any(BookFormDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author.fullName").value("Author 1"))
                .andExpect(jsonPath("$.genre.name").value("Fiction"));
    }

    @Test
    @WithMockUser
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Mockito.verify(bookService).deleteById(1L);
    }
}