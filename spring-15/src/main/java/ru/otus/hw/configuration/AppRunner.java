package ru.otus.hw.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.OrderService;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {
    final OrderService orderService;

    @Override
    public void run(String... args) {
        orderService.startGenerateOrdersLoop();
    }
}