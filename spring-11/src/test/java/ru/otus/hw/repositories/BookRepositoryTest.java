package ru.otus.hw.repositories;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;


@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookRepositoryTest {

    private static final String NON_EXISTING_BOOK_ID = "999";
    @Autowired
    private BookRepository repository;

    @Test
    @Order(1)
    void shouldReturnAllBooks() {
        StepVerifier.create(repository.findAll())
                .expectNext(
                        new Book("1", "BookTitle_1", new Author("1", "Author_1"), new Genre("1", "Genre_1")),
                        new Book("2", "BookTitle_2", new Author("2", "Author_2"), new Genre("2", "Genre_2"))
                )
                .verifyComplete();
    }

    @Test
    @Order(2)
    void shouldReturnBookById() {
        StepVerifier.create(repository.findById("1"))
                .expectNextMatches(book ->
                        book.getId().equals("1") &&
                                book.getTitle().equals("BookTitle_1") &&
                                book.getAuthor() != null &&
                                book.getAuthor().getId().equals("1") &&
                                book.getAuthor().getFullName().equals("Author_1") &&
                                book.getGenre() != null &&
                                book.getGenre().getId().equals("1") &&
                                book.getGenre().getName().equals("Genre_1")
                )
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        StepVerifier.create(repository.findById(NON_EXISTING_BOOK_ID))
                .verifyComplete();
    }

    @Test
    @Order(3)
    void shouldInsertBook() {
        Author newAuthor = new Author("3", "Author_3");
        Genre newGenre = new Genre("3", "Genre_3");

        repository.save(new Book(null, "InsertedBook", newAuthor, newGenre))
                .block();

        StepVerifier.create(repository.findAll())
                .expectNextMatches(book -> book.getTitle().equals("BookTitle_1"))
                .expectNextMatches(book -> book.getTitle().equals("BookTitle_2"))
                .expectNextMatches(book -> book.getTitle().equals("InsertedBook"))
                .verifyComplete();
    }

    @Test
    @Order(4)
    void shouldUpdateBook() {
        StepVerifier.create(
                        repository.findById("1")
                                .flatMap(book -> {
                                    book.setTitle("UpdatedTitle");
                                    return repository.save(book);
                                })
                )
                .expectNext(new Book("1", "UpdatedTitle", new Author("1", "Author_1"), new Genre("1", "Genre_1")))
                .verifyComplete();
    }

    @Test
    void shouldDeleteBookById() {
        StepVerifier.create(repository.deleteById("2")
                        .then(repository.findById("2")))
                .verifyComplete();
    }
}