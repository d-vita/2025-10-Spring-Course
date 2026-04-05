package ru.otus.hw.services;

import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.dto.TariffFormDto;

import java.util.List;

public interface TariffService {

    List<TariffDto> findAll();

    TariffDto insert(TariffFormDto tariffFormDto);
}
