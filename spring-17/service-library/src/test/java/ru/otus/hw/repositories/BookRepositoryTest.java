package ru.otus.hw.repositories;

import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    private static final long FIRST_BOOK_ID = 1L;
    private static final int EXPECTED_QUERIES_COUNT = 1;
    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;
    private static final long NON_EXISTING_BOOK_ID = 999L;
    @Autowired
    private BookRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldFindAllBooksWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        var book = repository.findAll();

        assertThat(book).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> b.getAuthor().getFullName() != null)
                .allMatch(b -> b.getGenre().getName() != null);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    void shouldFindBookById() {
        val actualBook = repository.findById(FIRST_BOOK_ID);
        val expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(actualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(NON_EXISTING_BOOK_ID)).isEmpty();
    }

    @Test
    void shouldInsertBook() {
        var author = em.find(Author.class, 1L);
        var genre = em.find(Genre.class, 1L);

        var book = new Book(0L, "InsertedBook", author, genre);

        var saved = repository.save(book);
        assertThat(saved.getId()).isGreaterThan(3L);

        var actual = repository.findById(saved.getId());
        assertThat(actual).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    void shouldUpdateBook() {
        var author = em.find(Author.class, 2L);
        var genre = em.find(Genre.class, 2L);

        var updated = new Book(1L, "UpdatedTitle", author, genre);

        repository.save(updated);

        var actual = repository.findById(1L);
        assertThat(actual).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(updated);
    }

    @Test
    void shouldDeleteBookById() {
        repository.deleteById(2L);
        assertThat(repository.findById(2L)).isEmpty();
    }
}