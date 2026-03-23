package ru.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
public class NotificationRequest {

    private Long userId;

    private String message;

}
