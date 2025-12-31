package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.models.Book;


@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookDto fromDomainObject(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                authorConverter.fromDomainObject(book.getAuthor()),
                genreConverter.fromDomainObject(book.getGenre())
        );
    }

    public BookFormDto toFormDto(BookDto bookDto) {
        return new BookFormDto(
                bookDto.id(),
                bookDto.title(),
                bookDto.author().id(),
                bookDto.genre().id()
        );
    }
}
