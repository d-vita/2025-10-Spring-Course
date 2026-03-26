package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentFormDto;
import ru.otus.hw.dto.NotificationDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
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
    public CommentDto insert(CommentFormDto commentFormDto) {
        var book = bookRepository.findById(commentFormDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book with id %d not found".formatted(commentFormDto.bookId()))
                );

        var comment = commentRepository.save(new Comment(0, commentFormDto.message(), book));

        sendNotification(comment, book);

        return commentConverter.fromDomainObject(comment);
    }

    @CircuitBreaker(name = "serviceCircuitBreaker", fallbackMethod = "notificationFallback")
    public void sendNotification(Comment comment, Book book) {
        notificationService.send(new NotificationDto(
                book.getAuthor().getId(),
                "New comment added: " + comment.getMessage()
        ));
    }

    public void notificationFallback(Comment comment, Book book, Throwable ex) {
        log.warn("Notification service unavailable, comment saved anyway: {}", ex.getMessage());
    }

    private Comment getComment(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }
}
