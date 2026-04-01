package ru.otus.hw.repositories;

import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Tariff;
import ru.otus.hw.models.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    private static final long FIRST_USER_ID = 1L;
    private static final int EXPECTED_NUMBER_OF_USERS = 1;
    private static final long NON_EXISTING_USER_ID = 999L;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldFindAllUsersWithTariff() {
        SessionFactory sessionFactory = em.getEntityManager()
                .getEntityManagerFactory()
                .unwrap(SessionFactory.class);

        sessionFactory.getStatistics().setStatisticsEnabled(true);

        var users = repository.findAll();

        assertThat(users)
                .isNotNull()
                .hasSize(EXPECTED_NUMBER_OF_USERS)
                .allMatch(u -> u.getUsername() != null)
                .allMatch(u -> u.getEmail() != null)
                .allMatch(u -> u.getTariff() != null)
                .allMatch(u -> u.getTariff().getName() != null);

        // благодаря @EntityGraph должен быть 1 запрос
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(1);
    }

    @Test
    void shouldFindUserById() {
        val actualUser = repository.findById(FIRST_USER_ID);
        val expectedUser = em.find(User.class, FIRST_USER_ID);

        assertThat(actualUser).isPresent().get()
                .usingRecursiveComparison()
                .isEqualTo(expectedUser);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(NON_EXISTING_USER_ID)).isEmpty();
    }

    @Test
    void shouldInsertUser() {
        Tariff tariff = em.find(Tariff.class, 1L);

        User user = new User(
                "new_user",
                "new_user@example.com",
                "password",
                tariff
        );

        var saved = repository.save(user);

        assertThat(saved.getId()).isNotNull();

        var actual = repository.findById(saved.getId());

        assertThat(actual).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    void shouldUpdateUser() {
        Tariff tariff = em.find(Tariff.class, 1L);

        User user = repository.findById(FIRST_USER_ID).orElseThrow();

        user.setUsername("updated_user");
        user.setEmail("updated@example.com");
        user.setPasswordHash("new_password");
        user.setTariff(tariff);

        repository.save(user);

        var actual = repository.findById(FIRST_USER_ID);

        assertThat(actual).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void shouldDeleteUserById() {
        repository.deleteById(FIRST_USER_ID);

        assertThat(repository.findById(FIRST_USER_ID)).isEmpty();
    }
}