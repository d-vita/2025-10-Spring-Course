package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.dto.CommentDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnAllBooks() {
        List<BookDto> books = webTestClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(books).isNotNull().isNotEmpty();

        assertThat(books)
                .extracting(BookDto::title)
                .contains("BookTitle_1", "BookTitle_2");
    }

    @Test
    void shouldReturnBookById() {
        String bookId = "1";

        BookDto book = webTestClient.get()
                .uri("/api/books/{id}", bookId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(book).isNotNull();
        assertThat(book.title()).isEqualTo("BookTitle_1");
        assertThat(book.author().fullName()).isEqualTo("Author_1");
        assertThat(book.genre().name()).isEqualTo("Genre_1");
    }

    @Test
    void shouldReturnCommentsForBook() {
        String bookId = "1";

        var comments = webTestClient.get()
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

    @Test
    void shouldCreateBook() {
        BookFormDto form = new BookFormDto("New Book", "1", "1");

        BookDto created = webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(form), BookFormDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(created).isNotNull();
        assertThat(created.title()).isEqualTo("New Book");
        assertThat(created.author().fullName()).isEqualTo("Author_1");
        assertThat(created.genre().name()).isEqualTo("Genre_1");
    }

    @Test
    void shouldUpdateBook() {
        BookFormDto formCreate = new BookFormDto("Temp Book", "2", "2");

        BookDto created = webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(formCreate), BookFormDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .returnResult()
                .getResponseBody();

        assert created != null;
        String bookId = created.id();

        BookFormDto formUpdate = new BookFormDto("Updated Book", "3", "3");

        BookDto updated = webTestClient.put()
                .uri("/api/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(formUpdate), BookFormDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(updated).isNotNull();
        assertThat(updated.title()).isEqualTo("Updated Book");
        assertThat(updated.author().fullName()).isEqualTo("Author_3");
        assertThat(updated.genre().name()).isEqualTo("Genre_3");
    }

    @Test
    void shouldDeleteBook() {
        BookFormDto formCreate = new BookFormDto("To Delete", "1", "1");

        BookDto created = webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(formCreate), BookFormDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .returnResult()
                .getResponseBody();

        assert created != null;
        String bookId = created.id();

        webTestClient.delete()
                .uri("/api/books/{id}", bookId)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/api/books/{id}", bookId)
                .exchange()
                .expectStatus().isNotFound();
    }
}