package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.services.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<UserDto> users = List.of(
                new UserDto(1L, "john_doe", "john@example.com", "FREE"),
                new UserDto(2L, "jane_doe", "jane@example.com", "BASIC")
        );

        Mockito.when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("john_doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[0].tariff").value("FREE"));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        UserDto user = new UserDto(1L, "john_doe", "john@example.com", "FREE");

        Mockito.when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.tariff").value("FREE"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserFormDto form = new UserFormDto(
                "john_doe",
                "john@example.com",
                "password123",
                1L
        );

        UserDto created = new UserDto(1L, "john_doe", "john@example.com", "FREE");

        Mockito.when(userService.insert(any(UserFormDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.tariff").value("FREE"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserFormDto form = new UserFormDto(
                "updated_user",
                "updated@example.com",
                "password123",
                1L
        );

        UserDto updated = new UserDto(1L, "updated_user", "updated@example.com", "FREE");

        Mockito.when(userService.update(eq(1L), any(UserFormDto.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updated_user"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.tariff").value("FREE"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteById(1L);
    }
}