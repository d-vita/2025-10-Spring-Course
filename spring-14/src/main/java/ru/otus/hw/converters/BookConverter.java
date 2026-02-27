package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.jpa.Book;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;


@RequiredArgsConstructor
@Component
public class BookConverter {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    public BookMongo fromJPAtoMongo(Book bookJPA) {
        AuthorMongo authorMongo = getAuthor(bookJPA);
        GenreMongo genreMongo = getGenre(bookJPA);

        BookMongo book = new BookMongo();
        book.setTitle(bookJPA.getTitle());
        book.setAuthorMongo(authorMongo);
        book.setGenreMongo(genreMongo);
        return book;
    }

    private AuthorMongo getAuthor(Book bookJPA) {
        return authorRepository
                .findByFullName(bookJPA.getAuthor().getFullName())
                .stream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Author with name %s not found".formatted(bookJPA.getAuthor().getFullName())
                ));
    }

    private GenreMongo getGenre(Book bookJPA) {
        return genreRepository
                .findByName(bookJPA.getGenre().getName())
                .stream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(bookJPA.getGenre().getName())));
    }
}
