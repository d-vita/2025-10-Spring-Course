package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.TariffConverter;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.dto.TariffFormDto;
import ru.otus.hw.exceptions.TariffAlreadyExistsException;
import ru.otus.hw.models.Tariff;
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

    @Override
    public TariffDto insert(TariffFormDto tariffFormDto) {
        if (tariffRepository.existsByName(tariffFormDto.name())) {
            throw new TariffAlreadyExistsException(
                    "Tariff with name %s already exists".formatted(tariffFormDto.name())
            );
        }

        Tariff tariff = new Tariff(
                null,
                tariffFormDto.name(),
                tariffFormDto.maxLinks(),
                tariffFormDto.maxClicksPerLink()
        );
        return tariffConverter.fromDomainObject(tariffRepository.save(tariff));
    }
}
