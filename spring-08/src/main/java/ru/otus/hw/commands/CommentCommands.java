package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    @ShellMethod(value = "Find all comments on book", key = "acb")
    public String findAllByBookId(String bookId) {
        return commentService.findByBookId(bookId).stream()
                .map(CommentDto::toString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(String id) {
        return commentService.findById(id)
                .map(CommentDto::toString)
                .orElse("Comment with id %s not found".formatted(id));
    }

    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertBook(String message, String bookId) {
        var savedComment = commentService.insert(message, bookId);
        return savedComment.toString();
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateBook(String id, String message, String bookId) {
        var savedComment = commentService.update(id, message, bookId);
        return savedComment.toString();
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteBook(String id) {
        commentService.deleteById(id);
    }
}
