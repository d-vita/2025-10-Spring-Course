package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.kafka.producer.UserDeletedEventProducer;
import ru.otus.hw.kafka.producer.UserRegisteredEventProducer;
import ru.otus.hw.kafka.producer.UserTariffUpdatedEventProducer;
import ru.otus.hw.models.Tariff;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.TariffRepository;
import ru.otus.hw.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Implementation Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TariffRepository tariffRepository;

    @Mock
    private UserConverter userConverter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDeletedEventProducer userDeletedEventProducer;

    @Mock
    private UserRegisteredEventProducer userRegisteredEventProducer;

    @Mock
    private UserTariffUpdatedEventProducer userTariffUpdatedEventProducer;

    @InjectMocks
    private UserServiceImpl userService;

    private Tariff testTariff;
    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testTariff = new Tariff();
        testTariff.setId(1L);
        testTariff.setName("Basic");
        testTariff.setMaxLinks(10L);
        testTariff.setMaxClicksPerLink(100L);

        testUser = new User("testuser", "test@example.com", "encodedPassword", testTariff);
        testUser.setId(1L);

        TariffDto tariffDto = new TariffDto(1L, "Basic", 10L, 100L);
        testUserDto = new UserDto(1L, "testuser", "test@example.com", tariffDto);
    }

    @Test
    @DisplayName("Should find user by id")
    void shouldFindUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userConverter.fromDomainObject(testUser)).thenReturn(testUserDto);

        UserDto result = userService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.username()).isEqualTo("testuser");
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User with id 999 not found");
    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAllUsers() {
        User user2 = new User("user2", "user2@example.com", "password", testTariff);
        user2.setId(2L);

        when(userRepository.findAll()).thenReturn(List.of(testUser, user2));
        when(userConverter.fromDomainObject(any(User.class)))
                .thenReturn(testUserDto)
                .thenReturn(new UserDto(2L, "user2", "user2@example.com",
                        new TariffDto(1L, "Basic", 10L, 100L)));

        List<UserDto> results = userService.findAll();

        assertThat(results).hasSize(2);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should insert new user")
    void shouldInsertNewUser() {
        UserFormDto userFormDto = new UserFormDto(
                "newuser",
                "newuser@example.com",
                "password123",
                1L
        );

        when(tariffRepository.findById(1L)).thenReturn(Optional.of(testTariff));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userConverter.fromDomainObject(testUser)).thenReturn(testUserDto);

        UserDto result = userService.insert(userFormDto);

        assertThat(result).isNotNull();
        verify(tariffRepository).findById(1L);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(userRegisteredEventProducer).sendUserRegisteredEvent(any(User.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when tariff not found during insert")
    void shouldThrowExceptionWhenTariffNotFoundDuringInsert() {
        UserFormDto userFormDto = new UserFormDto(
                "newuser",
                "newuser@example.com",
                "password123",
                999L
        );

        when(tariffRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.insert(userFormDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Tariff with id 999 not found");
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() {
        UserFormDto updateDto = new UserFormDto(
                "updateduser",
                "updated@example.com",
                "newpassword",
                1L
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(tariffRepository.findById(1L)).thenReturn(Optional.of(testTariff));
        when(passwordEncoder.encode("newpassword")).thenReturn("newEncodedPassword");
        when(userConverter.fromDomainObject(testUser)).thenReturn(testUserDto);

        UserDto result = userService.update(1L, updateDto);

        assertThat(result).isNotNull();
        assertThat(testUser.getUsername()).isEqualTo("updateduser");
        assertThat(testUser.getEmail()).isEqualTo("updated@example.com");
        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode("newpassword");
    }

    @Test
    @DisplayName("Should update user and send tariff updated event when tariff changed")
    void shouldUpdateUserAndSendEventWhenTariffChanged() {
        Tariff newTariff = new Tariff();
        newTariff.setId(2L);
        newTariff.setName("Premium");
        newTariff.setMaxLinks(10L);
        newTariff.setMaxClicksPerLink(100L);

        UserFormDto updateDto = new UserFormDto(
                "updateduser",
                "updated@example.com",
                "password",
                2L
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(tariffRepository.findById(2L)).thenReturn(Optional.of(newTariff));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userConverter.fromDomainObject(testUser)).thenReturn(testUserDto);

        userService.update(1L, updateDto);

        verify(userTariffUpdatedEventProducer).sendUserTariffUpdatedEvent(testUser);
    }

    @Test
    @DisplayName("Should not send tariff updated event when tariff not changed")
    void shouldNotSendEventWhenTariffNotChanged() {
        UserFormDto updateDto = new UserFormDto(
                "updateduser",
                "updated@example.com",
                "password",
                1L
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(tariffRepository.findById(1L)).thenReturn(Optional.of(testTariff));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userConverter.fromDomainObject(testUser)).thenReturn(testUserDto);

        userService.update(1L, updateDto);

        verify(userTariffUpdatedEventProducer, never()).sendUserTariffUpdatedEvent(any(User.class));
    }

    @Test
    @DisplayName("Should not update password when password is blank")
    void shouldNotUpdatePasswordWhenBlank() {
        UserFormDto updateDto = new UserFormDto(
                "updateduser",
                "updated@example.com",
                "",
                1L
        );

        String originalPassword = testUser.getPasswordHash();
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(tariffRepository.findById(1L)).thenReturn(Optional.of(testTariff));
        when(userConverter.fromDomainObject(testUser)).thenReturn(testUserDto);

        userService.update(1L, updateDto);

        assertThat(testUser.getPasswordHash()).isEqualTo(originalPassword);
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Should delete user by id")
    void shouldDeleteUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        userService.deleteById(1L);

        verify(userRepository).deleteById(1L);
        verify(userDeletedEventProducer).sendUserDeletedEvent(1L, "testuser");
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existing user")
    void shouldThrowExceptionWhenDeletingNonExistingUser() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User with id 999 not found");

        verify(userRepository, never()).deleteById(anyLong());
    }
}
