package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class BookRepositoryTest {

    private static final String FIRST_BOOK_ID = "1";
    private static final String SECOND_BOOK_ID = "2";
    private static final String THIRD_BOOK_ID = "3";
    private static final String NON_EXISTING_BOOK_ID = "999";
    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldFindAllBooksWithAllInfo() {
        val firstBook = mongoTemplate.findById(FIRST_BOOK_ID, Book.class);
        val secondBook = mongoTemplate.findById(SECOND_BOOK_ID, Book.class);
        val thirdBook = mongoTemplate.findById(THIRD_BOOK_ID, Book.class);

        var books = bookRepository.findAll();

        assertThat(books).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
                        .containsExactlyInAnyOrder(firstBook, secondBook, thirdBook);
    }

    @Test
    void shouldFindBookById() {
        val actualBook = bookRepository.findById(FIRST_BOOK_ID);
        val expectedBook = mongoTemplate.findById(FIRST_BOOK_ID, Book.class);

        assertThat(actualBook).isPresent().get().isEqualTo(expectedBook);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(bookRepository.findById(NON_EXISTING_BOOK_ID)).isEmpty();
    }

    @Test
    @DirtiesContext
    void shouldInsertBook() {
        var author = mongoTemplate.findById("1", Author.class);
        var genre = mongoTemplate.findById("1", Genre.class);

        var book = new Book(null, "InsertedBook", author, genre);

        var saved = bookRepository.save(book);
        assertThat(saved.getId()).isGreaterThan("3");

        var actual = bookRepository.findById(saved.getId());
        assertThat(actual).isPresent().get().isEqualTo(saved);
    }

    @Test
    @DirtiesContext
    void shouldUpdateBook() {
        var author = mongoTemplate.findById("2", Author.class);
        var genre = mongoTemplate.findById("2", Genre.class);

        var updated = new Book("1", "UpdatedTitle", author, genre);

        bookRepository.save(updated);

        var actual = bookRepository.findById("1");
        assertThat(actual).isPresent().get().isEqualTo(updated);
    }

    @Test
    @DirtiesContext
    void shouldDeleteBookById() {
        bookRepository.deleteById("2");
        assertThat(bookRepository.findById("2")).isEmpty();
    }
}