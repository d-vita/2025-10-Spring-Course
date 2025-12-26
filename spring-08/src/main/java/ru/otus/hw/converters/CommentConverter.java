package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    public CommentDto toDto(Comment comment) {
        var book = bookRepository.findById(comment.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found: " + comment.getBookId()));

        var bookDto = bookConverter.toDto(book);

        return new CommentDto(
                comment.getId(),
                comment.getMessage(),
                bookDto
        );
    }
}
