package ru.otus.hw.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Event sent when a user's tariff is updated.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTariffUpdatedEvent {

    private Long userId;

    private String tariffName;

    private Long maxClicksPerLink;

    private Long maxLinks;

    private Instant timestamp;
}
