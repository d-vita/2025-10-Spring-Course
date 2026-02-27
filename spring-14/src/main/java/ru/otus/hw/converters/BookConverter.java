package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

//    public BookDto fromDomainObject(Book book) {
//        return new BookDto(
//                book.getId(),
//                book.getTitle(),
//                authorConverter.fromDomainObject(book.getAuthor()),
//                genreConverter.fromDomainObject(book.getGenre())
//        );
//    }
}
