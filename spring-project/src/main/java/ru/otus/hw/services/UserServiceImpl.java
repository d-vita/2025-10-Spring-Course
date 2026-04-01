package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.TariffRepository;
import ru.otus.hw.repositories.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final TariffRepository tariffRepository;

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(long id) {
        return userConverter.fromDomainObject(
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("User with id %d not found".formatted(id))
                        )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userConverter::fromDomainObject)
                .toList();
    }

    @Override
    @Transactional
    public UserDto insert(UserFormDto userFormDto) {
        return userConverter.fromDomainObject(save(null, userFormDto));
    }

    @Override
    @Transactional
    public UserDto update(long id, UserFormDto userFormDto) {
        return userConverter.fromDomainObject(save(id, userFormDto));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id %d not found".formatted(id));
        }
        userRepository.deleteById(id);
    }

    private User save(Long id, UserFormDto userFormDto) {
        var tariff = tariffRepository.findById(userFormDto.tariffId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Tariff with id %d not found".formatted(userFormDto.tariffId()))
                );

        User user;

        if (id == null) {
            user = new User(userFormDto.username(), userFormDto.email(), userFormDto.password(), tariff);
        } else {
            user = userRepository.findById(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException("User with id %d not found".formatted(id))
                    );

            user.setUsername(userFormDto.username());
            user.setEmail(userFormDto.email());
            user.setPasswordHash(userFormDto.password());
            user.setTariff(tariff);
        }

        return userRepository.save(user);
    }
}
