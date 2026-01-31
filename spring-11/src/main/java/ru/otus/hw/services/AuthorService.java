package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    Flux<AuthorDto> findAll();
}
