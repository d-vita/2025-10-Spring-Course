package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;


@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookDto fromDomainObject(Book book, Author author, Genre genre) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                authorConverter.fromDomainObject(author),
                genreConverter.fromDomainObject(genre)
        );
    }
}
