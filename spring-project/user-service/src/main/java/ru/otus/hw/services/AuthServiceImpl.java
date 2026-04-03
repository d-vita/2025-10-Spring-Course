package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.dto.LoginRequest;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Tariff;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.TariffRepository;
import ru.otus.hw.repositories.UserRepository;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserConverter userConverter;

    private final UserRepository userRepository;

    private final TariffRepository tariffRepository;

    private final PasswordEncoder passwordEncoder;

    public User register(UserFormDto dto) {

        Tariff tariff = tariffRepository.findById(dto.tariffId())
                .orElseThrow(() -> new EntityNotFoundException("Tariff not found"));

        User user = userConverter.fromDto(dto);

        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setTariff(tariff);

        return user;
    }

    @Override
    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() ->
                        new EntityNotFoundException("User with username %s not found".formatted(loginRequest.username()))
                );

        if (passwordEncoder.matches(loginRequest.password(),  user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }
}
