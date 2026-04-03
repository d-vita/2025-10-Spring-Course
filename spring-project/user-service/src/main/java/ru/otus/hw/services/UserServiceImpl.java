package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Tariff;
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

    private final PasswordEncoder passwordEncoder;

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
        var tariff = getTariff(userFormDto.tariffId());

        User user = new User(
                userFormDto.username(),
                userFormDto.email(),
                passwordEncoder.encode(userFormDto.password()),
                tariff
        );

        return userConverter.fromDomainObject(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(long id, UserFormDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with id %d not found".formatted(id))
                );

        var tariff = getTariff(dto.tariffId());

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setTariff(tariff);

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(dto.password()));
        }

        return userConverter.fromDomainObject(user);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id %d not found".formatted(id));
        }
        userRepository.deleteById(id);
    }

    private Tariff getTariff(Long tariffId) {
        return tariffRepository.findById(tariffId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Tariff with id %d not found".formatted(tariffId))
                );
    }
}
