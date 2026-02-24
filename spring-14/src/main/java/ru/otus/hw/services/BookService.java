package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto insert(BookFormDto bookDto);

    BookDto update(long id, BookFormDto bookDto);

    void deleteById(long id);
}
