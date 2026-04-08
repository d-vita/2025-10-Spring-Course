package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.dto.TariffFormDto;
import ru.otus.hw.services.TariffService;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;

    @GetMapping
    public List<TariffDto> getTariffs() {
        return tariffService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TariffDto create(@RequestBody @Valid TariffFormDto tariffFormDto) {
        return tariffService.insert(tariffFormDto);
    }
}