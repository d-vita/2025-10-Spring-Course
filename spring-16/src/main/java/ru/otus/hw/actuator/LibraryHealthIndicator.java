package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.configuration.LibraryProperties;
import ru.otus.hw.repositories.BookRepository;


@RequiredArgsConstructor
@Component
public class LibraryHealthIndicator implements HealthIndicator  {

    private final BookRepository bookRepository;

    private final LibraryProperties properties;

    @Override
    public Health health() {
        try {
            long books = bookRepository.count();
            return buildHealth(books);
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }

    private Health buildHealth(long books) {
        return switchStatus(books)
                .withDetail("books", books)
                .withDetail("inventoryMessage", getInventoryMessage(books))
                .build();
    }

    private Health.Builder switchStatus(long books) {
        var t = properties.getThresholds();

        if (books == t.getEmpty()) {
            return Health.down();
        }
        if (books < t.getReplenish()) {
            return Health.status("REPLENISHMENT_REQUIRED");
        }
        return Health.up();
    }

    private String getInventoryMessage(long books) {
        var t = properties.getThresholds();
        var m = properties.getMessage();

        return books < t.getCritical() ? m.getEmpty()
                : books < t.getLow() ? m.getLow()
                : m.getOk();
    }

}
