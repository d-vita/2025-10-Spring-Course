package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.configuration.SecurityConfiguration;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CustomUserDetailsService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    // ---------------- GET ----------------

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getBooksAllowedForUser() throws Exception {
        Mockito.when(bookService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());
    }

    @Test
    void getBooksUnauthorizedWithoutUser() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
    }

    // ---------------- POST ----------------

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void postBookAllowedForAdmin() throws Exception {
        BookDto created = new BookDto(1L, "New Book", new AuthorDto(1L, "Author 1"), new GenreDto(1L, "Fiction"));
        Mockito.when(bookService.insert(any(BookFormDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Book"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void postBookForbiddenForUser() throws Exception {
        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "editor", roles = {"EDITOR"})
    void postBookForbiddenForEditor() throws Exception {
        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isForbidden());
    }

    // ---------------- PUT ----------------

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void putBookAllowedForAdmin() throws Exception {
        BookDto updated = new BookDto(1L, "Updated Book", new AuthorDto(1L, "Author 1"), new GenreDto(1L, "Fiction"));
        Mockito.when(bookService.update(eq(1L), any(BookFormDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/books/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }

    @Test
    @WithMockUser(username = "editor", roles = {"EDITOR"})
    void putBookAllowedForEditor() throws Exception {
        BookDto updated = new BookDto(1L, "Updated Book", new AuthorDto(1L, "Author 1"), new GenreDto(1L, "Fiction"));
        Mockito.when(bookService.update(eq(1L), any(BookFormDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/books/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void putBookForbiddenForUser() throws Exception {
        mockMvc.perform(put("/api/books/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isForbidden());
    }

    // ---------------- DELETE ----------------

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBookAllowedForAdmin() throws Exception {
        mockMvc.perform(delete("/api/books/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
        Mockito.verify(bookService).deleteById(1L);
    }

    @Test
    @WithMockUser(username = "editor", roles = {"EDITOR"})
    void deleteBookForbiddenForEditor() throws Exception {
        mockMvc.perform(delete("/api/books/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteBookForbiddenForUser() throws Exception {
        mockMvc.perform(delete("/api/books/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}