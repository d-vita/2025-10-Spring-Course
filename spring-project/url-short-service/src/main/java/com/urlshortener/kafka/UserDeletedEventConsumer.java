package com.urlshortener.kafka;

import com.urlshortener.event.UserDeletedEvent;
import com.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDeletedEventConsumer {

    private final UrlService urlService;

    @KafkaListener(topics = "user-deleted", groupId = "url-service-group")
    public void consumeUserDeletedEvent(UserDeletedEvent event) {
        urlService.deleteAllByUserId(event.getUserId());
    }
}
