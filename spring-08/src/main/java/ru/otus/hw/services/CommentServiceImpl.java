package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentConverter commentConverter;

    private final BookRepository bookRepository;

    private final BookService bookService;

    @Override
    public Optional<CommentDto> findById(String id) {
        return commentRepository.findById(id).map(this::mapToCommentDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId).stream()
                .map(this::mapToCommentDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto insert(String message, String bookId) {
        return mapToCommentDto(save(null, message, bookId));
    }

    @Override
    @Transactional
    public CommentDto update(String id, String message, String bookId) {
        return mapToCommentDto(save(id, message, bookId));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Comment save(String id, String message, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(id, message, book.getId());
        return commentRepository.save(comment);
    }

    private CommentDto mapToCommentDto(Comment comment) {
        BookDto bookDto = bookService.findById(comment.getBookId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id %s not found".formatted(comment.getBookId())));
        return commentConverter.toCommentDto(comment, bookDto);
    }
}
