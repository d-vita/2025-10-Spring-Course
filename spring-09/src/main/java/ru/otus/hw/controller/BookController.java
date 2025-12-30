package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;



@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/")
    public String getBooks(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/{id}/edit")
    public String getEditBookPage(@PathVariable long id, Model model) {
        var book = bookService.findById(id).orElseThrow(NotFoundException::new);
        var authors = authorService.findAll();
        var genres = genreService.findAll();

        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);

        return "edit";
    }

    @PostMapping("/{id}/edit")
    public String updateBook(@PathVariable long id,
                             @Valid @ModelAttribute("book") BookFormDto bookForm,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "edit";
        }

        bookService.update(id, bookForm.title(), bookForm.authorId(), bookForm.genreId());
        return "redirect:/";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/create")
    public String addPage(Model model) {
        return "add";
    }

    @PostMapping("/create")
    public String saveBook(@RequestParam String title,
                           @RequestParam("author.id") long authorId,
                           @RequestParam("genre.id") long genreId) {
        bookService.insert(title, authorId, genreId);
        return "redirect:/";
    }
}
