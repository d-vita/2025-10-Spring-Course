package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.models.User;


@RequiredArgsConstructor
@Component
public class UserConverter {

    private final TariffConverter tariffConverter;

    public UserDto fromDomainObject(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                tariffConverter.fromDomainObject(user.getTariff()).name()
        );
    }
}
