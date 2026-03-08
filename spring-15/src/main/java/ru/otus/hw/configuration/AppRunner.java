package ru.otus.hw.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.OrderService;

@Configuration
@RequiredArgsConstructor
public class AppRunner {

    private final OrderService orderService;

    @Bean
    CommandLineRunner startLoop() {
        return args -> orderService.startGenerateOrdersLoop();
    }
}