package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
public class BookBatchConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookBatchConfig.class);

    private final BatchProperties batchProperties;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    private final EntityManagerFactory entityManagerFactory;

    private final MongoTemplate mongoTemplate;

    private final BookMigrationService bookMigrationService;


    @Bean
    public Job importBookJob(Step bookStep) {
        return new JobBuilder("importBookJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // позволяет запускать job повторно
                .flow(bookStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        LOGGER.info(
                                "Job '{}' started at {}",
                                jobExecution.getJobInstance().getJobName(),
                                jobExecution.getStartTime());
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        LOGGER.info(
                                "Job '{}' finished with status {}",
                                jobExecution.getJobInstance().getJobName(),
                                jobExecution.getExitStatus());
                    }
                })
                .build();
    }

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
