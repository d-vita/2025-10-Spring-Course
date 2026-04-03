package ru.otus.hw.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthResponse;
import ru.otus.hw.dto.LoginRequest;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.models.User;
import ru.otus.hw.services.AuthService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@RequestBody @Valid UserFormDto userFormDto) {
        //TODO: Add validation for existing username/email
        //TODO: Publish UserRegisteredEvent to Kafka

        User user = authService.register(userFormDto);

        // TODO: Generate real JWT token
        String mockToken = "mock-jwt-token" + user.getId();

        return new AuthResponse(user.getId(), user.getUsername(), mockToken);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        User user = authService.login(loginRequest);

        // TODO: Generate real JWT token
        String mockToken = "mock-jwt-token" + user.getId();

        return new AuthResponse(user.getId(), user.getUsername(), mockToken);
    }
}
