package ru.demo.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Notification {

    private Long userId;

    private String message;

}
