package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.services.TariffService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TariffController.class)
public class TariffControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TariffService tariffService;

    @Test
    void shouldReturnAllAuthors() throws Exception {
        List<TariffDto> authors = List.of(
                new TariffDto(1L, "FREE"),
                new TariffDto(2L, "BASIC")
        );
        Mockito.when(tariffService.findAll()).thenReturn(authors);

        mockMvc.perform(get("/api/tariffs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(authors.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("FREE"))
                .andExpect(jsonPath("$[1].name").value("BASIC"));
    }
}
