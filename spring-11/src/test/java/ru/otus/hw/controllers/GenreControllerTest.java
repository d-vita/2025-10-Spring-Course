package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.hw.dto.GenreDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnAllGenres() {
        List<GenreDto> genres = webTestClient.get()
                .uri("/api/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(genres).isNotNull().isNotEmpty();

        assertThat(genres)
                .extracting(GenreDto::name)
                .contains("Genre_1", "Genre_2", "Genre_3");
    }
}
