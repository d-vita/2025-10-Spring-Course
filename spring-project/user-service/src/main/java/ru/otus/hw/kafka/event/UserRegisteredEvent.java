package ru.otus.hw.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Event published when a new user registers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {

    private Long userId;

    private String username;

    private String email;

    private String tariffName;

    private Long maxClicksPerLink;

    private Long maxLinks;

    private Instant registeredAt;

}
