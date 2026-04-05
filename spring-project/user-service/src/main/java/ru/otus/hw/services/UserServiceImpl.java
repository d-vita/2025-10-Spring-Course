package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.kafka.producer.UserRegisteredEventProducer;
import ru.otus.hw.kafka.producer.UserTariffUpdatedEventProducer;
import ru.otus.hw.models.Tariff;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.TariffRepository;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.kafka.producer.UserDeletedEventProducer;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final TariffRepository tariffRepository;

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    private final PasswordEncoder passwordEncoder;

    private final UserDeletedEventProducer userDeletedEventProducer;

    private final UserRegisteredEventProducer userRegisteredEventProducer;

    private final UserTariffUpdatedEventProducer userTariffUpdatedEventProducer;

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

        User savedUser = userRepository.save(user);
        userRegisteredEventProducer.sendUserRegisteredEvent(savedUser);

        return userConverter.fromDomainObject(savedUser);
    }

    @Override
    @Transactional
    public UserDto update(long id, UserFormDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with id %d not found".formatted(id))
                );

        var tariff = getTariff(dto.tariffId());
        boolean tariffChanged = !user.getTariff().getId().equals(tariff.getId());

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setTariff(tariff);

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(dto.password()));
        }

        if (tariffChanged) {
            userTariffUpdatedEventProducer.sendUserTariffUpdatedEvent(user);
        }

        return userConverter.fromDomainObject(user);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with id %d not found".formatted(id))
                );
        userRepository.deleteById(id);
        userDeletedEventProducer.sendUserDeletedEvent(user.getId(), user.getUsername());
    }

    private Tariff getTariff(Long tariffId) {
        return tariffRepository.findById(tariffId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Tariff with id %d not found".formatted(tariffId))
                );
    }
}
