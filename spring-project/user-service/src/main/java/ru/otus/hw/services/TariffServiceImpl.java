package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.TariffConverter;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.repositories.TariffRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;

    private final TariffConverter tariffConverter;

    @Override
    @Transactional(readOnly = true)
    public List<TariffDto> findAll() {
        return tariffRepository.findAll().stream()
                .map(tariffConverter::fromDomainObject)
                .toList();
    }
}
