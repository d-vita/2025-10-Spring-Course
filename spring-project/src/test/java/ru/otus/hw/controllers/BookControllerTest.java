package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.services.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService bookService;

    @Test
    void shouldReturnAllBooks() throws Exception {
        List<UserDto> books = List.of(
                new UserDto(1L, "Book 1", new TariffDto(1L, "Author 1"), new GenreDto(1L, "Fiction")),
                new UserDto(2L, "Book 2", new TariffDto(2L, "Author 2"), new GenreDto(2L, "Science"))
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
    void shouldReturnBookById() throws Exception {
        UserDto book = new UserDto(1L, "Book 1", new TariffDto(1L, "Author 1"), new GenreDto(1L, "Fiction"));
        Mockito.when(bookService.findById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book 1"))
                .andExpect(jsonPath("$.author.fullName").value("Author 1"))
                .andExpect(jsonPath("$.genre.name").value("Fiction"));
    }

    @Test
    void shouldCreateBook() throws Exception {
        UserFormDto form = new UserFormDto("New Book", 1L, 1L);
        UserDto created = new UserDto(1L, "New Book", new TariffDto(1L, "Author 1"), new GenreDto(1L, "Fiction"));
        Mockito.when(bookService.insert(any(UserFormDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author.fullName").value("Author 1"))
                .andExpect(jsonPath("$.genre.name").value("Fiction"));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        UserFormDto form = new UserFormDto("Updated Book", 1L, 1L);
        UserDto updated = new UserDto(1L, "Updated Book", new TariffDto(1L, "Author 1"), new GenreDto(1L, "Fiction"));

        Mockito.when(bookService.update(eq(1L), any(UserFormDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author.fullName").value("Author 1"))
                .andExpect(jsonPath("$.genre.name").value("Fiction"));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(bookService).deleteById(1L);
    }
}