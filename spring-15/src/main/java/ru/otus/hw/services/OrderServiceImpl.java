package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.BookRequest;
import ru.otus.hw.domain.Shipment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final int numOfBook = 3;

    private final BookShopGateway shop;

    public OrderServiceImpl(BookShopGateway shop) {
        this.shop = shop;
    }

    @Override
    public void startGenerateOrdersLoop() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < 10; i++) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            int num = i + 1;
            pool.execute(() -> {
                Collection<BookRequest> requests = generateBookRequests();

                Collection<Shipment> shipments = shop.process(requests);

                String deliveredBooks = shipments.stream()
                        .map(s -> {
                            BookRequest r = s.getOrder().getRequest();
                            return "!!!!!!!!!!!!!BookId:" + r.getBookId() + (r.isVip() ? "(VIP)" : "");
                        })
                        .collect(Collectors.joining(", "));

                System.out.println("!!!!!!!!!!!!Batch #" + num + ", Delivered books: " + deliveredBooks);
            });
            delay();
        }
    }

    private static Collection<BookRequest> generateBookRequests() {
        Collection<BookRequest> requests = new ArrayList<>();

        for (long i = 1; i <= numOfBook; i++) {
            boolean isVip = ThreadLocalRandom.current().nextBoolean();

            BookRequest br = new BookRequest(
                    i,
                    ThreadLocalRandom.current().nextLong(1, 100),
                    "Address" + i,
                    isVip
            );
            requests.add(br);
        }
        return requests;
    }

    private void delay() {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
