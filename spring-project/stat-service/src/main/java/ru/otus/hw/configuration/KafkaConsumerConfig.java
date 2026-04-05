package ru.otus.hw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public DefaultErrorHandler errorHandler() {
        var handler = new DefaultErrorHandler(
                (record, exception) -> {
                    System.err.println("Deserialization error for message: " + record + ", reason: " + exception.getMessage());
                }
        );

        handler.setRetryListeners((record, ex, deliveryAttempt) ->
                System.out.println("Attempt " + deliveryAttempt + " for " + record.key())
        );

        return handler;
    }
}
