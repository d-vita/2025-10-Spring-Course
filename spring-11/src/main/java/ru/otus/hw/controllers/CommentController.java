package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

    @GetMapping("/{bookId}/comments")
    public Flux<CommentDto> getCommentsByBook(@PathVariable String bookId) {
        return commentRepository.findAllByBookId(bookId)
                .map(commentConverter::fromDomainObject);
    }
}
