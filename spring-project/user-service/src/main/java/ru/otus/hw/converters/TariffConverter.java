package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.models.Tariff;

@Component
public class TariffConverter {

    public TariffDto fromDomainObject(Tariff tariff) {
        return new TariffDto(
                tariff.getId(),
                tariff.getName(),
                tariff.getMaxLinks(),
                tariff.getMaxClicksPerLink()
        );
    }
}
