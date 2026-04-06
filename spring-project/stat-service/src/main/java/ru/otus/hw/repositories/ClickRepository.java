package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Click;

import java.util.Optional;

public interface ClickRepository extends JpaRepository<Click, String> {

    Optional<Click> findByUserIdAndShortUrl(Long userId, String shortUrl);

    @Modifying
    @Query("UPDATE Click c SET c.userId = -1 WHERE c.userId = :userId")
    void anonymizeByUserId(@Param("userId") Long userId);
}
