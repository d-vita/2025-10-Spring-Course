package com.urlshortener.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Event consumed when a user is deleted.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDeletedEvent {

    private Long userId;

    private String username;

    private Instant deletedAt;

}
