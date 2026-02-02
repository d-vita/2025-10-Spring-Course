package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.hw.dto.CommentDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnAllGenres() {
        String bookId = "1";

        List<CommentDto> comments = webTestClient.get()
                .uri("/api/books/{bookId}/comments", bookId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(comments).isNotNull().isNotEmpty();

        assertThat(comments)
                .extracting(CommentDto::message)
                .contains("Comment1", "Comment3");
    }
}
