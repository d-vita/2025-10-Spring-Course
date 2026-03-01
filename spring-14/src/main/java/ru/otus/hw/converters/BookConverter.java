package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.jpa.Book;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.GenreMongo;


@RequiredArgsConstructor
@Component
public class BookConverter {

    public BookMongo fromJPAtoMongo(Book bookJPA, AuthorMongo authorMongo, GenreMongo genreMongo) {
        BookMongo book = new BookMongo();
        book.setTitle(bookJPA.getTitle());
        book.setAuthorMongo(authorMongo);
        book.setGenreMongo(genreMongo);
        return book;
    }
}
