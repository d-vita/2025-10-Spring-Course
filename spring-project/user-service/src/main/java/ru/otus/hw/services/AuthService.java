package ru.otus.hw.services;

import ru.otus.hw.dto.LoginRequest;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.models.User;

public interface AuthService {

    User register(UserFormDto dto);

    User login(LoginRequest loginRequest);
}
