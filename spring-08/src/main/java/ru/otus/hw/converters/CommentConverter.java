package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    public CommentDto toCommentDto(Comment comment, BookDto bookDto) {
        return new CommentDto(
                comment.getId(),
                comment.getMessage(),
                bookDto
        );
    }
}
