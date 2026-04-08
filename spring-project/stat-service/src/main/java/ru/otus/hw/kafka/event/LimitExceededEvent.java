package ru.otus.hw.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Event sent when a user exceeds their tariff limits.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitExceededEvent {

    private Long userId;

    private String shortUrl;

    private LimitType limitType;

    private long currentValue;

    private long limitValue;

    private Instant timestamp;

    public enum LimitType {
        CLICKS,
        URLS_CREATED
    }
}
