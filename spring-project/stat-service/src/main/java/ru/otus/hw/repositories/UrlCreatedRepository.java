package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Click;
import ru.otus.hw.models.UrlCreated;

import java.util.Optional;

public interface UrlCreatedRepository extends JpaRepository<UrlCreated, Long> {
}
