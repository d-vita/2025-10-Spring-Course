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
@Table(name = "click_stats")
public class Click {

    @Id
    private String id; // shortUrl + ":" + userId

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String shortUrl;

    @Column(nullable = false)
    private long clicks;

    private Instant lastClickAt;
}
