package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.services.BookService;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;
}
