package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.services.BookService;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/books/{id}")
    public BookDto getBook(@PathVariable long id) {
        return bookService.findById(id);
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookDto> saveBook(@RequestBody @Valid BookFormDto bookForm) {
        var book = bookService.insert(
                bookForm.title(),
                bookForm.authorId(),
                bookForm.genreId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PutMapping("/api/books/{id}")
    public BookDto updateBook(
            @PathVariable long id,
            @RequestBody @Valid BookFormDto bookForm
    ) {
        return bookService.update(
                id,
                bookForm.title(),
                bookForm.authorId(),
                bookForm.genreId());
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
