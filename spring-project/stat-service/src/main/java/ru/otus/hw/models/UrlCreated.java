package ru.otus.hw.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "url_created_stats")
public class UrlCreated {

    @Id
    private Long userId;

    @Column(name = "urls_created")
    private Long urlsCreated;

    @Column(name = "last_created_at")
    private Instant lastCreatedAt;
}
