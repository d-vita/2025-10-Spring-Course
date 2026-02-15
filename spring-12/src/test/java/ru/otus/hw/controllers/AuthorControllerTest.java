package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

//    @Test
//    void shouldReturnAllAuthors() throws Exception {
//        List<AuthorDto> authors = List.of(
//                new AuthorDto(1L, "Author 1"),
//                new AuthorDto(2L, "Author 2")
//        );
//        Mockito.when(authorService.findAll()).thenReturn(authors);
//
//        mockMvc.perform(get("/api/authors"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(authors.size()))
//                .andExpect(jsonPath("$[0].id").value(1L))
//                .andExpect(jsonPath("$[0].fullName").value("Author 1"))
//                .andExpect(jsonPath("$[1].fullName").value("Author 2"));
//    }
}
