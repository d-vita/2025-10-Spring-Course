package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentFormDto;
import ru.otus.hw.dto.NotificationDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentConverter commentConverter;

    private final BookRepository bookRepository;

    private final NotificationService notificationService;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "serviceCircuitBreaker")
    public CommentDto findById(long id) {
        return commentConverter.fromDomainObject(getComment(id));
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "serviceCircuitBreaker")
    public List<CommentDto> findAllByBookId(long bookId) {
        return commentRepository.findAllByBookId(bookId)
                .stream()
                .map(commentConverter::fromDomainObject)
                .toList();
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "serviceCircuitBreaker")
    public CommentDto insert(CommentFormDto commentFormDto) {
        return commentConverter.fromDomainObject(save(0, commentFormDto));
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "serviceCircuitBreaker")
    public CommentDto update(long id, CommentFormDto commentFormDto) {
        return commentConverter.fromDomainObject(save(id, commentFormDto));
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "serviceCircuitBreaker")
    public void deleteById(long id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("Comment with id %d not found".formatted(id));
        }
        commentRepository.deleteById(id);
    }

    private Comment getComment(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    private Comment save(long id, CommentFormDto commentFormDto) {
        var book = bookRepository.findById(commentFormDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(commentFormDto.bookId())));

        var comment = commentRepository.save(new Comment(id, commentFormDto.message(), book));

        notificationService.send(new NotificationDto(
                book.getAuthor().getId(),
                "New comment added: " + commentFormDto.message()));

        return comment;
    }
}
