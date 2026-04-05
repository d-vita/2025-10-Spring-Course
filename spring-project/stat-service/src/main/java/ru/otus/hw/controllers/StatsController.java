package ru.otus.hw.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.services.ClickService;
import ru.otus.hw.services.UrlCreatedService;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final ClickService clickService;

    private final UrlCreatedService urlCreatedService;

    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserStats(@PathVariable Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("urlsCreated", urlCreatedService.getUrlsCreated(userId));
        return stats;
    }

    @GetMapping("/user/{userId}/url/{shortUrl}/clicks")
    public Map<String, Object> getUrlClickStats(
            @PathVariable Long userId,
            @PathVariable String shortUrl) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("clicks", clickService.getClicks(userId, shortUrl));
        return stats;
    }

}
