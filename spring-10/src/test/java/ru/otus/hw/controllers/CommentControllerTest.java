package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void shouldReturnCommentsForBook() throws Exception {
        long bookId = 1L;
        List<CommentDto> comments = List.of(
                new CommentDto(1L, "Great book", bookId)
        );
        Mockito.when(commentService.findAllByBookId(bookId)).thenReturn(comments);

        mockMvc.perform(get("/api/books/{bookId}/comments", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(comments.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].message").value("Great book"))
                .andExpect(jsonPath("$[0].bookId").value(bookId));
    }
}
