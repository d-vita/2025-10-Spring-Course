package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.jpa.Book;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

@Service
@RequiredArgsConstructor
public class BookMigrationService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookConverter bookConverter;

    public BookMongo migrate(Book bookJPA) {
        AuthorMongo authorMongo = authorRepository
                .findByFullName(bookJPA.getAuthor().getFullName())
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        GenreMongo genreMongo = genreRepository
                .findByName(bookJPA.getGenre().getName())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        return bookConverter.fromJPAtoMongo(bookJPA, authorMongo, genreMongo);
    }
}
