package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final BookConverter bookConverter;

    @Override
    public Optional<BookDto> findById(String id) {
        return bookRepository.findById(id).map(this::mapToBookDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(this::mapToBookDto)
                .toList();
    }

    @Override
    @Transactional
    public BookDto insert(String title, String authorId, String genreId) {
        return mapToBookDto(save(null, title, authorId, genreId));
    }

    @Override
    @Transactional
    public BookDto update(String id, String title, String authorId, String genreId) {
        return mapToBookDto(save(id, title, authorId, genreId));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        if (commentRepository.existsByBookId(id)) {
            commentRepository.deleteAllByBookId(id);
        }
        bookRepository.deleteById(id);
    }

    private Book save(String id, String title, String authorId, String genreId) {
        var author = getAuthor(authorId);
        var genre = getGenre(genreId);
        var book = new Book(id, title, author.getId(), genre.getId());
        return bookRepository.save(book);
    }

    private Author getAuthor(String authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
    }

    private Genre getGenre(String genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
    }

    private BookDto mapToBookDto(Book book) {
        return bookConverter.toBookDto(book, getAuthor(book.getAuthorId()), getGenre(book.getGenreId()));
    }
}
