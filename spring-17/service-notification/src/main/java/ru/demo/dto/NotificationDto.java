package ru.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationDto {

    @NotNull
    private Long userId;

    @NotNull
    private String message;

}
