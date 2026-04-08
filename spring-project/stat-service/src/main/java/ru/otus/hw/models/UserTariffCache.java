package ru.otus.hw.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_tariff_cache")
public class UserTariffCache {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "max_clicks_per_link", nullable = false)
    private Long maxClicksPerLink;

    @Column(name = "max_links", nullable = false)
    private Long maxLinks;

    @Column(name = "tariff_name", length = 100)
    private String tariffName;
}