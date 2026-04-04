package ru.otus.hw.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Event published when e new user registers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {

    private Long userId;

    private String username;

    private String email;

    private Instant registeredAt;

}
