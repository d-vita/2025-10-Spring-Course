package ru.otus.hw.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.LoginDto;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.services.AuthService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody @Valid UserFormDto userFormDto) {
        //TODO: Publish UserRegisteredEvent to Kafka
        // TODO: Generate real JWT token

        return authService.register(userFormDto);
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody @Valid LoginDto loginDto) {
        // TODO: Generate real JWT token

        return authService.login(loginDto);
    }
}
