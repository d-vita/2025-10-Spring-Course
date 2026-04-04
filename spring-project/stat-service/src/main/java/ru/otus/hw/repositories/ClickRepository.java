package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Click;

import java.util.Optional;

public interface ClickRepository extends JpaRepository<Click, Long> {

    Optional<Click> findByUserIdAndShortUrl(Long userId, String shortUrl);
}
