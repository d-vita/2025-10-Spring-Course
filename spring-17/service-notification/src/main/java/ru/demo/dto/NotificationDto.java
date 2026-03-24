package ru.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationDto {

    @NotNull
    private String userId;

    @NotNull
    private String message;
}
