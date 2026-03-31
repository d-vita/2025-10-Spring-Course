package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.services.UserService;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final UserService bookService;

    @GetMapping
    public List<UserDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto getBook(@PathVariable long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserFormDto bookFormDto) {
        return bookService.insert(bookFormDto);
    }

    @PutMapping("/{id}")
    public UserDto update(
            @PathVariable long id,
            @RequestBody @Valid UserFormDto bookFormDto
    ) {
        return bookService.update(id, bookFormDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
    }
}
