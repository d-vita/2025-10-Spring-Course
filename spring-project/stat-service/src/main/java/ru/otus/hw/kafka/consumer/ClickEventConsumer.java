package ru.otus.hw.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.kafka.event.ClickEvent;
import ru.otus.hw.services.ClickService;

@Slf4j
@RequiredArgsConstructor
@Component
public class ClickEventConsumer {

    private final ClickService clickService;

    @KafkaListener(topics = "url-clicks", groupId = "stat-service-group")
    public void consumeClickEvent(ClickEvent event) {
        log.info("!!!!!!!!!!url-clicks {} {}", event.getShortUrl(), event.getUserId());
        clickService.recordClick(event.getShortUrl(), event.getUserId());
    }
}
