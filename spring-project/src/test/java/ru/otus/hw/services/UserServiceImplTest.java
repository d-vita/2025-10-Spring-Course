package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.TariffConverter;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.dto.TariffDto;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({UserServiceImpl.class, UserConverter.class, TariffConverter.class})
@Transactional(propagation = Propagation.NEVER)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldReturnAllUsers() {
        List<UserDto> users = userService.findAll();
        assertThat(users).isNotEmpty();
        assertThat(users).allMatch(u -> u.username() != null && u.email() != null);
    }

    @Test
    void shouldReturnUserById() {
        UserDto user = userService.findAll().get(0);
        UserDto found = userService.findById(user.id());

        assertThat(found).isEqualTo(user);
        assertThat(found.tariff()).isNotNull();
    }

    @Test
    void shouldCreateUser() {
        UserFormDto form = new UserFormDto(
                "new_user",
                "new_user@example.com",
                "password123",
                1L
        );

        UserDto created = userService.insert(form);
        assertThat(created.id()).isPositive();
        assertThat(created.username()).isEqualTo("new_user");
        assertThat(created.tariff()).isEqualTo("FREE");

        List<UserDto> all = userService.findAll();
        assertThat(all).contains(created);
    }

    @Test
    void shouldUpdateUser() {
        UserDto existing = userService.findAll().get(0);

        UserFormDto form = new UserFormDto(
                "updated_name",
                existing.email(),
                "new_password",
                1L
        );

        UserDto updated = userService.update(existing.id(), form);

        assertThat(updated.id()).isEqualTo(existing.id());
        assertThat(updated.username()).isEqualTo("updated_name");
    }

    @Test
    void shouldDeleteUser() {
        UserDto existing = userService.findAll().get(0);

        userService.deleteById(existing.id());

        assertThatThrownBy(() -> userService.findById(existing.id()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User with id");
    }
}