package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public String listPage(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        BookDto book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        return "edit";
    }

    @PostMapping(value = "/edit", params = "update")
    public String updateBook(BookDto book) {
        bookService.update(book.id(), book.title(), book.author().id(), book.genre().id());
        return "redirect:/";
    }

    @PostMapping(value = "/edit", params = "delete")
    public String deleteBook(BookDto book) {
        bookService.deleteById(book.id());
        return "redirect:/";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        return "add";
    }

    @PostMapping("/add")
    public String saveBook(@RequestParam String title,
                           @RequestParam("author.id") long authorId,
                           @RequestParam("genre.id") long genreId) {
        bookService.insert(title, authorId, genreId);
        return "redirect:/";
    }
}
