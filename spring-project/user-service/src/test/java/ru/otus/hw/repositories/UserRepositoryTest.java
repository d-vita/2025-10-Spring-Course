package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Tariff;
import ru.otus.hw.models.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("User Repository Tests")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Should save and find user by id")
    void shouldSaveAndFindUserById() {
        Tariff tariff = new Tariff();
        tariff.setName("Basic");
        tariff.setMaxLinks(10L);
        tariff.setMaxClicksPerLink(100L);
        em.persist(tariff);

        User user = new User("testuser", "test@example.com", "password", tariff);
        User savedUser = userRepository.save(user);
        em.flush();
        em.clear();

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.get().getTariff()).isNotNull();
        assertThat(foundUser.get().getTariff().getName()).isEqualTo("Basic");
    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAllUsers() {
        Tariff tariff = new Tariff();
        tariff.setName("Basic");
        tariff.setMaxLinks(10L);
        tariff.setMaxClicksPerLink(100L);
        em.persist(tariff);

        User user1 = new User("user1", "user1@example.com", "password1", tariff);
        User user2 = new User("user2", "user2@example.com", "password2", tariff);

        userRepository.save(user1);
        userRepository.save(user2);
        em.flush();

        List<User> users = userRepository.findAll();

        assertThat(users).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Should delete user by id")
    void shouldDeleteUserById() {
        Tariff tariff = new Tariff();
        tariff.setName("Basic");
        tariff.setMaxLinks(10L);
        tariff.setMaxClicksPerLink(100L);
        em.persist(tariff);

        User user = new User("testuser", "test@example.com", "password", tariff);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();
        em.flush();

        userRepository.deleteById(userId);
        em.flush();

        Optional<User> deletedUser = userRepository.findById(userId);
        assertThat(deletedUser).isEmpty();
    }

    @Test
    @DisplayName("Should find user by username")
    void shouldFindUserByUsername() {
        Tariff tariff = new Tariff();
        tariff.setName("Basic");
        tariff.setMaxLinks(10L);
        tariff.setMaxClicksPerLink(100L);
        em.persist(tariff);

        User user = new User("uniqueuser", "unique@example.com", "password", tariff);
        userRepository.save(user);
        em.flush();

        Optional<User> foundUser = userRepository.findByUsername("uniqueuser");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("uniqueuser");
    }

    @Test
    @DisplayName("Should return empty when user not found by username")
    void shouldReturnEmptyWhenUserNotFoundByUsername() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        assertThat(foundUser).isEmpty();
    }
}
