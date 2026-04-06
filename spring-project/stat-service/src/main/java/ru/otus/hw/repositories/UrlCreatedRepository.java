package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.UrlCreated;


public interface UrlCreatedRepository extends JpaRepository<UrlCreated, Long> {

    @Modifying
    @Query("UPDATE UrlCreated u SET u.userId = -1 WHERE u.userId = :userId")
    void anonymizeByUserId(@Param("userId") Long userId);
}
