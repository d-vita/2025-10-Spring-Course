package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.jpa.Book;
import ru.otus.hw.models.mongo.BookMongo;


@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookMongo fromJPAtoMongo(Book bookJPA) {
        BookMongo book = new BookMongo();
        book.setAuthorMongo(authorConverter.fromJPAtoMongo(bookJPA.getAuthor()));
        book.setGenreMongo(genreConverter.fromJPAtoMongo(bookJPA.getGenre()));
        return book;
    }
}
