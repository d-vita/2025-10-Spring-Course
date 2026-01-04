package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book from book id " + id + " not found"));
        return bookConverter.fromDomainObject(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookConverter::fromDomainObject)
                .toList();
    }

    @Override
    @Transactional
    public BookDto insert(BookFormDto bookFormDto) {
        return bookConverter.fromDomainObject(save(
                0, bookFormDto));
    }

    @Override
    @Transactional
    public BookDto update(long id, BookFormDto bookFormDto) {
        return bookConverter.fromDomainObject(save(
                id, bookFormDto));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, BookFormDto bookFormDto) {
        var author = authorRepository.findById(bookFormDto.authorId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Author with id %d not found".formatted(bookFormDto.authorId())));
        var genre = genreRepository.findById(bookFormDto.genreId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Genre with id %d not found".formatted(bookFormDto.genreId())));
        var book = new Book(id, bookFormDto.title(), author, genre);
        return bookRepository.save(book);
    }
}
