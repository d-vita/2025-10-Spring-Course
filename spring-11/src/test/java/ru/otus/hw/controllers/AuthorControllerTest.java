package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.hw.dto.AuthorDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnAllAuthors() {
        List<AuthorDto> authors = webTestClient.get()
                .uri("/api/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(authors).isNotNull().isNotEmpty();

        assertThat(authors)
                .extracting(AuthorDto::fullName)
                .contains("Author_1", "Author_2", "Author_3");
    }
}
