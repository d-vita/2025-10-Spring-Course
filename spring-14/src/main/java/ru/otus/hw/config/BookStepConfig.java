package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.jpa.Book;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.services.BookMigrationService;

@RequiredArgsConstructor
@Configuration
public class BookStepConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookStepConfig.class);

    private final JobProperties batchProperties;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    private final EntityManagerFactory entityManagerFactory;

    private final MongoTemplate mongoTemplate;

    private final BookMigrationService bookMigrationService;


    @Bean
    public Step bookStep() {
        return new StepBuilder("bookStep", jobRepository)
                .<Book, BookMongo>chunk(batchProperties.getChunkSize(), transactionManager)
                .reader(bookReader())
                .processor(bookProcessor())
                .writer(bookWriter(mongoTemplate))
                .listener(new ChunkListener() {
                    @Override
                    public void afterChunkError(@NonNull ChunkContext context) {
                        LOGGER.error("Chunk failed.");
                    }
                })
                .build();
    }

    @Bean
    public JpaPagingItemReader<Book> bookReader() {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(batchProperties.getPageSize())
                .queryString("SELECT b FROM Book b ORDER BY b.id ASC")
                .build();
    }

    @Bean
    public ItemProcessor<Book, BookMongo> bookProcessor() {
        return bookMigrationService::migrate;
    }

    @Bean
    public MongoItemWriter<BookMongo> bookWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<BookMongo>()
                .template(mongoTemplate)
                .collection("books")
                .build();
    }
}
