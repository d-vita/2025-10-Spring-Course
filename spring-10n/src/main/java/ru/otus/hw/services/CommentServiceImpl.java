package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentConverter commentConverter;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(long id) {
        return commentRepository.findById(id).map(commentConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByBookId(long bookId) {
        return commentRepository.findAllByBookId(bookId).stream()
                .map(commentConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto insert(String message, long bookId) {
        return commentConverter.toDto(save(0, message, bookId));
    }

    @Override
    @Transactional
    public CommentDto update(long id, String message, long bookId) {
        return commentConverter.toDto(save(id, message, bookId));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String message, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, message, book);
        return commentRepository.save(comment);
    }
}
