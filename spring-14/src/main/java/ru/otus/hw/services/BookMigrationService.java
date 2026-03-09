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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BookMigrationService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookConverter bookConverter;

    private final Map<Long, AuthorMongo> authorCache = new ConcurrentHashMap<>();

    private final Map<Long, GenreMongo> genreCache = new ConcurrentHashMap<>();

    public void putAuthor(Long jpaId, AuthorMongo authorMongo) {
        authorCache.put(jpaId, authorMongo);
    }

    public void putGenre(Long jpaId, GenreMongo genreMongo) {
        genreCache.put(jpaId, genreMongo);
    }

    public BookMongo migrate(Book bookJPA) {
        AuthorMongo authorMongo = authorCache.get(bookJPA.getAuthor().getId());
        if (authorMongo == null) {
            throw new EntityNotFoundException(
                    "Author mapping not found for id=" + bookJPA.getAuthor().getId());
        }
        GenreMongo genreMongo = genreCache.get(bookJPA.getGenre().getId());
        if (genreMongo == null) {
            throw new EntityNotFoundException(
                    "Genre mapping not found for id=" + bookJPA.getGenre().getId());
        }
        return bookConverter.fromJPAtoMongo(bookJPA, authorMongo, genreMongo);
    }
}
