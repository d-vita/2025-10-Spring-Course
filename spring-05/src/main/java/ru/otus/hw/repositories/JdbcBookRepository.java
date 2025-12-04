package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<Book> findById(long id) {
        String query = """
                SELECT
                   b.id AS id,
                   b.title AS title,
                   a.id AS author_id,
                   a.full_name AS author_full_name,
                   g.id AS genre_id,
                   g.name AS genre_name
                FROM books b
                JOIN authors a ON b.author_id = a.id
                JOIN genres g  ON b.genre_id = g.id  
                WHERE b.id = :id 
                """;
        try {
            Book book = jdbcTemplate.queryForObject(query, Map.of("id", id), new BookRowMapper());
            return Optional.of(book);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<Book> findAll() {
        String query = """
                SELECT
                   b.id AS id,
                   b.title AS title,
                   a.id AS author_id,
                   a.full_name AS author_full_name,
                   g.id AS genre_id,
                   g.name AS genre_name
                FROM books b
                JOIN authors a ON b.author_id = a.id
                JOIN genres g  ON b.genre_id = g.id
                """;
        return jdbcTemplate.query(query, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        int rows = jdbcTemplate.update(
                "DELETE FROM books WHERE id = :id",
                Map.of("id", id)
        );

        if (rows == 0) {
            throw new EntityNotFoundException("Book not found: id=" + id);
        }
    }

    private Book insert(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId())
                .addValue("genre_id", book.getGenre().getId());

        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update("""
                INSERT INTO books (title, author_id, genre_id)
                VALUES (:title, :author_id, :genre_id)
                        """,
                params, keyHolder, new String[]{"id"});

        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        int rows = jdbcTemplate.update("""
                UPDATE books
                SET title = :title, author_id = :author_id, genre_id = :genre_id
                WHERE id = :id
                """,
                Map.of(
                        "title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId(),
                        "id", book.getId()
                )
        );

        if (rows == 0) {
            throw new EntityNotFoundException("Book not found: id=" + book.getId());
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(
                    rs.getLong("id"),
                    rs.getString("title"),
                    new Author(
                            rs.getLong("author_id"),
                            rs.getString("author_full_name")
                    ),
                    new Genre(
                            rs.getLong("genre_id"),
                            rs.getString("genre_name")
                    )
            );
        }
    }
}
