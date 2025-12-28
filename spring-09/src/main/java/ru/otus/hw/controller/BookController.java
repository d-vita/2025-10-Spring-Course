package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookDto;
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

//    @GetMapping("/edit")
//    public String editPage(@RequestParam("id") long id, Model model) {
//        Person person = repository.findById(id).orElseThrow(NotFoundException::new);
//        model.addAttribute("person", person);
//        return "edit";
//    }
//
//    @PostMapping("/edit")
//    public String savePerson(Person person) {
//        repository.save(person);
//        return "redirect:/";
//    }
}
