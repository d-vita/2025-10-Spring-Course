package ru.otus.hw.services;

import ru.otus.hw.dto.TariffDto;

import java.util.List;

public interface TariffService {

    List<TariffDto> findAll();
}
