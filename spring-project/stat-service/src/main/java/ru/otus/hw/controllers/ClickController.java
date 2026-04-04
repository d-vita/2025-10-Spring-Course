package ru.otus.hw.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clicks")
public class ClickController {

    private final ClickService clickService;

    @PostMapping
    public ResponseEntity<Void> recordClick(@RequestBody ClickRequest request) {
        clickService.recordClick(request.getShortUrl(), request.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/{shortUrl}")
    public ResponseEntity<Long> getClicks(
            @PathVariable Long userId,
            @PathVariable String shortUrl) {
        long count = clickService.getClicks(userId, shortUrl);
        return ResponseEntity.ok(count);
    }
}
