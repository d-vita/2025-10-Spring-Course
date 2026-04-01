package ru.otus.hw.repositories;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.enums.TariffName;
import ru.otus.hw.models.Tariff;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TariffRepositoryTest {
//
//    private static final int EXPECTED_NUMBER_OF_TARIFFS = 3;
//    private static final long FIRST_TARIFF_ID = 1L;
//    private static final long NON_EXISTING_TARIFF_ID = 999L;
//
//    @Autowired
//    private TariffRepository repository;
//
//    @Autowired
//    private TestEntityManager em;
//
//    @Test
//    void shouldFindAllAuthors() {
//        var authors = repository.findAll();
//
//        assertThat(authors)
//                .isNotNull()
//                .hasSize(EXPECTED_NUMBER_OF_TARIFFS)
//                .allMatch(t -> t.getName() != null);
//
//        assertThat(authors)
//                .usingRecursiveComparison()
//                .isEqualTo(List.of(
//                        new Tariff(1L, TariffName.BASIC, 10, 1000),
//                        new Tariff(1L, TariffName.FREE, 10, 1000),
//                        new Tariff(1L, TariffName.PRO, 10, 1000),
//                ));
//    }
//
//    @Test
//    void shouldFindAuthorById() {
//        var actualAuthor = repository.findById(FIRST_AUTHOR_ID);
//        var expectedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
//
//        assertThat(actualAuthor).isPresent().get()
//                .usingRecursiveComparison().isEqualTo(expectedAuthor);
//    }
//
//    @Test
//    void shouldReturnEmptyWhenNotFound() {
//        assertThat(repository.findById(NON_EXISTING_AUTHOR_ID)).isEmpty();
//    }
}