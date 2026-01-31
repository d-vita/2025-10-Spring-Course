package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreRepository genreRepository;
    private final GenreConverter genreConverter;

    @GetMapping
    public Flux<GenreDto> getGenres() {
        return genreRepository.findAll()
                .map(genreConverter::fromDomainObject);
    }
}
