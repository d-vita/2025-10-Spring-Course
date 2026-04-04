package ru.otus.hw.services;

import ru.otus.hw.dto.LoginDto;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.models.User;

public interface AuthService {

    UserDto register(UserFormDto userFormDto);

    UserDto login(LoginDto loginDto);
}
