package ru.otus.hw.services;

import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;

import java.util.List;

public interface UserService {
    UserDto findById(long id);

    List<UserDto> findAll();

    UserDto insert(UserFormDto userFormDto);

    UserDto update(long id, UserFormDto userFormDto);

    void deleteById(long id);
}
