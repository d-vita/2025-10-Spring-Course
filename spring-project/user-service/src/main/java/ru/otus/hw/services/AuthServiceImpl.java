package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.dto.AuthResponse;
import ru.otus.hw.dto.LoginDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.InvalidCredentialsException;
import ru.otus.hw.exceptions.UserAlreadyExistsException;
import ru.otus.hw.models.Tariff;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.TariffRepository;
import ru.otus.hw.repositories.UserRepository;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final TariffRepository tariffRepository;

    private final UserConverter userConverter;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponse register(UserFormDto userFormDto) {

        if (userRepository.findByUsername(userFormDto.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        if (userRepository.findByEmail(userFormDto.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        Tariff tariff = tariffRepository.findById(userFormDto.tariffId())
                .orElseThrow(() -> new EntityNotFoundException("Tariff is not found"));

        User user = new User(
                userFormDto.username(),
                userFormDto.email(),
                passwordEncoder.encode(userFormDto.password()),
                tariff
        );

        try {
            User savedUser = userRepository.save(user);

            String token = jwtService.generateToken(savedUser);
            return new AuthResponse(
                    token,
                    userConverter.fromDomainObject(savedUser)
            );
        } catch (DataIntegrityViolationException ex) {
            throw new UserAlreadyExistsException("Email already exists");
        }
    }

    @Override
    public AuthResponse login(LoginDto loginDto) {

        User user = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(loginDto.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                userConverter.fromDomainObject(user)
        );
    }
}
