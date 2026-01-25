package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping("/{id}/comments")
    public String getCommentsByBook(@PathVariable long id, Model model) {
        var book = bookService.findById(id).orElseThrow(EntityNotFoundException::new);

        model.addAttribute("book", book);
        model.addAttribute("comments", commentService.findAllByBookId(id));
        return "comments";
    }
}
