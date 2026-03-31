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
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<UserDto> users = List.of(
                new UserDto(1L, "john_doe", "john@example.com", "password_hash", new TariffDto(1L, "FREE")),
                new UserDto(2L, "jane_doe", "jane@example.com", "password_hash", new TariffDto(2L, "BASIC"))
        );
        Mockito.when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(users.size()))
                .andExpect(jsonPath("$[0].username").value("john_doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[0].password").value("password_hash"))
                .andExpect(jsonPath("$[0].user.tariff.name").value("FREE"));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        UserDto book = new UserDto(1L,"john_doe", "john@example.com", "password_hash", new TariffDto(1L, "FREE"));
        Mockito.when(userService.findById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("john_doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[0].password").value("password_hash"))
                .andExpect(jsonPath("$[0].user.tariff.name").value("FREE"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserFormDto form = new UserFormDto("New Book", 1L, 1L);
        UserDto created = new UserDto(1L, "New Book", new TariffDto(1L, "Author 1"), new GenreDto(1L, "Fiction"));
        Mockito.when(userService.insert(any(UserFormDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author.fullName").value("Author 1"))
                .andExpect(jsonPath("$.genre.name").value("Fiction"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserFormDto form = new UserFormDto("Updated User", 1L, 1L);
        UserDto updated = new UserDto(1L, "Updated Book", new TariffDto(1L, "Author 1"), new GenreDto(1L, "Fiction"));

        Mockito.when(userService.update(eq(1L), any(UserFormDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Book\",\"authorId\":1,\"genreId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author.fullName").value("Author 1"))
                .andExpect(jsonPath("$.genre.name").value("Fiction"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteById(1L);
    }
}