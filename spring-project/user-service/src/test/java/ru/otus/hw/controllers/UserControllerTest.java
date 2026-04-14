package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.configuration.SecurityConfiguration;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.JwtService;
import ru.otus.hw.services.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
@DisplayName("User Controller Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("Should get all users")
    @WithMockUser
    void shouldGetAllUsers() throws Exception {
        TariffDto tariffDto = new TariffDto(1L, "Basic", 10L, 100L);
        List<UserDto> users = List.of(
                new UserDto(1L, "user1", "user1@example.com", tariffDto),
                new UserDto(2L, "user2", "user2@example.com", tariffDto)
        );

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    @DisplayName("Should get user by id")
    @WithMockUser
    void shouldGetUserById() throws Exception {
        TariffDto tariffDto = new TariffDto(1L, "Basic", 10L, 100L);
        UserDto userDto = new UserDto(1L, "user1", "user1@example.com", tariffDto);

        when(userService.findById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    @DisplayName("Should return 404 when user not found")
    @WithMockUser
    void shouldReturnNotFoundWhenUserNotExists() throws Exception {
        when(userService.findById(999L))
                .thenThrow(new EntityNotFoundException("User with id 999 not found"));

        mockMvc.perform(get("/api/users/999")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create user")
    @WithMockUser
    void shouldCreateUser() throws Exception {
        UserFormDto userFormDto = new UserFormDto(
                "newuser",
                "newuser@example.com",
                "password123",
                1L
        );

        TariffDto tariffDto = new TariffDto(1L, "Basic", 10L, 100L);
        UserDto createdUser = new UserDto(1L, "newuser", "newuser@example.com", tariffDto);

        when(userService.insert(any(UserFormDto.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    @DisplayName("Should return 400 when user data is invalid")
    @WithMockUser
    void shouldReturnBadRequestWhenUserDataInvalid() throws Exception {
        UserFormDto invalidDto = new UserFormDto(
                "",
                "invalid-email",
                "123",
                null
        );

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should update user")
    @WithMockUser
    void shouldUpdateUser() throws Exception {
        UserFormDto userFormDto = new UserFormDto(
                "updateduser",
                "updated@example.com",
                "newpassword123",
                1L
        );

        TariffDto tariffDto = new TariffDto(1L, "Basic", 10L, 100L);
        UserDto updatedUser = new UserDto(1L, "updateduser", "updated@example.com", tariffDto);

        when(userService.update(eq(1L), any(UserFormDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existing user")
    @WithMockUser
    void shouldReturnNotFoundWhenUpdatingNonExistingUser() throws Exception {
        UserFormDto userFormDto = new UserFormDto(
                "user",
                "user@example.com",
                "password123",
                1L
        );

        when(userService.update(eq(999L), any(UserFormDto.class)))
                .thenThrow(new EntityNotFoundException("User with id 999 not found"));

        mockMvc.perform(put("/api/users/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete user")
    @WithMockUser
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(userService).deleteById(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existing user")
    @WithMockUser
    void shouldReturnNotFoundWhenDeletingNonExistingUser() throws Exception {
        doThrow(new EntityNotFoundException("User with id 999 not found"))
                .when(userService).deleteById(999L);

        mockMvc.perform(delete("/api/users/999")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
