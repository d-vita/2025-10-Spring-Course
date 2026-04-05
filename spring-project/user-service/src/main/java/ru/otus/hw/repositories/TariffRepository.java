package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Tariff;
import ru.otus.hw.models.User;

import java.util.Optional;


public interface TariffRepository extends JpaRepository<Tariff, Long>  {

    Optional<Tariff> findByName(String name);

    boolean existsByName(String name);
}
