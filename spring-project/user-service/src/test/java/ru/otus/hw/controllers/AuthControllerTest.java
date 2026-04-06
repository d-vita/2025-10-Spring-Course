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
import ru.otus.hw.dto.AuthResponse;
import ru.otus.hw.dto.LoginDto;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.exceptions.InvalidCredentialsException;
import ru.otus.hw.exceptions.UserAlreadyExistsException;
import ru.otus.hw.services.AuthService;
import ru.otus.hw.services.JwtService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
@DisplayName("Auth Controller Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("Should register user successfully")
    @WithMockUser
    void shouldRegisterUserSuccessfully() throws Exception {
        UserFormDto userFormDto = new UserFormDto(
                "testuser",
                "test@example.com",
                "password123",
                1L
        );

        AuthResponse authResponse = new AuthResponse("test-token",
                new UserDto(1L,
                        "testuser",
                        "testuser@email.ru",
                        new TariffDto(1L, "FREE", 10L, 100L)));
        when(authService.register(any(UserFormDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.user.username").value("testuser"));
    }

    @Test
    @DisplayName("Should return 400 when registration data is invalid")
    @WithMockUser
    void shouldReturnBadRequestWhenRegistrationDataInvalid() throws Exception {
        UserFormDto invalidDto = new UserFormDto(
                "",
                "invalid-email",
                "123",
                null
        );

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 409 when user already exists")
    @WithMockUser
    void shouldReturnConflictWhenUserAlreadyExists() throws Exception {
        UserFormDto userFormDto = new UserFormDto(
                "existinguser",
                "existing@example.com",
                "password123",
                1L
        );

        when(authService.register(any(UserFormDto.class)))
                .thenThrow(new UserAlreadyExistsException("User already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should login successfully")
    @WithMockUser
    void shouldLoginSuccessfully() throws Exception {
        LoginDto loginDto = new LoginDto("testuser", "testuser@email.ru", "password123");
        AuthResponse authResponse = new AuthResponse("test-token",
                new UserDto(1L, "testuser", "testuser@email.ru",
                        new TariffDto(1L, "FREE", 10L, 100L)));

        when(authService.login(any(LoginDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.user.username").value("testuser"));
    }

    @Test
    @DisplayName("Should return 401 when credentials are invalid")
    @WithMockUser
    void shouldReturnUnauthorizedWhenCredentialsInvalid() throws Exception {
        LoginDto loginDto = new LoginDto("testuser", "testuser@email.ru", "wrongpassword");
        when(authService.login(any(LoginDto.class)))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 400 when login data is invalid")
    @WithMockUser
    void shouldReturnBadRequestWhenLoginDataInvalid() throws Exception {
        LoginDto invalidDto = new LoginDto("", "", "");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}
