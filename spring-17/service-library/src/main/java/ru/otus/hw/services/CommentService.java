package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentFormDto;

import java.util.List;

public interface CommentService {

    CommentDto findById(long id);

    List<CommentDto> findAllByBookId(long bookId);

    CommentDto insert(CommentFormDto commentFormDto);

    CommentDto update(long id, CommentFormDto commentFormDt);

    void deleteById(long id);
}
