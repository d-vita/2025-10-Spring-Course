package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    public BookDto toDto(Book book) {
        var author = authorRepository.findById(book.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found: " + book.getAuthorId()));
        var genre = genreRepository.findById(book.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found: " + book.getGenreId()));

        return new BookDto(
                book.getId(),
                book.getTitle(),
                new AuthorDto(author.getId(), author.getFullName()),
                new GenreDto(genre.getId(), genre.getName())
        );
    }
}
