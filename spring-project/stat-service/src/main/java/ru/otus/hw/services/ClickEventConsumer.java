package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.event.ClickEvent;

@RequiredArgsConstructor
@Component
public class ClickEventConsumer {

    private final ClickService clickService;

    @KafkaListener(topics = "url-clicks", groupId = "click-stats-group")
    public void consumeClickEvent(ClickEvent event) {
        clickService.recordClick(event.getShortUrl(), event.getUserId());
    }
}
