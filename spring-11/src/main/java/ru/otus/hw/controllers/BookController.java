package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @GetMapping
    public Flux<BookDto> getBooks() {
        return bookRepository.findAll()
                .map(bookConverter::fromDomainObject);
    }

    @GetMapping("/{id}")
    public Mono<BookDto> getBook(@PathVariable String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(id)
                        ))
                )
                .map(bookConverter::fromDomainObject);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> create(@RequestBody @Valid BookFormDto bookFormDto) {
        return save(null, bookFormDto)
                .map(bookConverter::fromDomainObject);
    }

    @PutMapping("/{id}")
    public Mono<BookDto> update(
            @PathVariable String id,
            @RequestBody @Valid BookFormDto bookFormDto
    ) {
        return save(id, bookFormDto)
                .map(bookConverter::fromDomainObject);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Book with id %s not found".formatted(id))
                ))
                .flatMap(bookRepository::delete);
    }

    private Mono<Book> save(String id, BookFormDto bookFormDto) {
        Mono<Author> authorMono = authorRepository.findById(bookFormDto.authorId())
                .switchIfEmpty(
                        Mono.error(
                                new EntityNotFoundException(
                                        "Author with id %s not found".formatted(bookFormDto.authorId())
                        ))
                );

        Mono<Genre> genreMono = genreRepository.findById(bookFormDto.genreId())
                .switchIfEmpty(
                        Mono.error(
                                new EntityNotFoundException(
                                        "Genre with id %s not found".formatted(bookFormDto.genreId())
                        ))
                );

        return Mono.zip(authorMono, genreMono)
                .map(tuple -> {
                    Author author = tuple.getT1();
                    Genre genre = tuple.getT2();
                    return new Book(id, bookFormDto.title(), author, genre);
                })
                .flatMap(bookRepository::save);
    }
}
