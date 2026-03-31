package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.services.TariffService;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;

    @GetMapping
    public List<TariffDto> getAuthors() {
        return tariffService.findAll();
    }
}