package ru.otus.hw.repositories;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT id, name FROM genres", new GnreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        Genre genre = jdbcTemplate.queryForObject("SELECT id, name FROM genres WHERE id = :id",
                Map.of("id", id), new GnreRowMapper());
        return Optional.of(genre);
    }

    private static class GnreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            return new Genre(
                    rs.getLong("id"),
                    rs.getString("name"));
        }
    }
}
