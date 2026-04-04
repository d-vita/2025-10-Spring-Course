package ru.otus.hw.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.services.ClickService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clicks")
public class ClickController {

    private final ClickService clickService;

    @GetMapping("/{userId}/{shortUrl}")
    public Long getClicks(
            @PathVariable Long userId,
            @PathVariable String shortUrl) {
        return clickService.getClicks(userId, shortUrl);
    }

}
