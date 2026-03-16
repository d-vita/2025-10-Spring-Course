package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;


@RequiredArgsConstructor
@Component
public class LibraryHealthIndicator implements HealthIndicator  {

    private final BookRepository bookRepository;

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
        if (books == 0) {
            return Health.down();
        }
        if (books < 20) {
            return Health.status("REPLENISHMENT_REQUIRED");
        }
        return Health.up();
    }

    private String getInventoryMessage(long books) {
        return books < 2 ? "Library inventory is almost empty!"
                : books < 10 ? "Inventory is low. Reprint books required."
                : "Inventory level is sufficient.";
    }

}
