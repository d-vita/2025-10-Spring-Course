package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
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
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.models.jpa.Author;
import ru.otus.hw.models.mongo.AuthorMongo;

@RequiredArgsConstructor
@Configuration
public class AuthorBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final MongoTemplate mongoTemplate;
    private final AuthorConverter authorConverter;

    private static final int CHUNK_SIZE = 5;

    @Bean
    public Job importAuthorJob() {
        return new JobBuilder("importAuthorJob", jobRepository)
                .start((authorStep()))
                .build();
    }

    @Bean
    public Step authorStep() {
        return new StepBuilder("authorStep", jobRepository)
                .<Author, AuthorMongo>chunk(CHUNK_SIZE, transactionManager)
                .reader(authorReader())
                .processor(authorProcessor())
                .writer(authorWriter(mongoTemplate))
                .build();
    }

    @Bean
    public JpaPagingItemReader<Author> authorReader() {
        return new JpaPagingItemReaderBuilder<Author>()
                .name("authorReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(10)
                .queryString("SELECT a FROM Author a ORDER BY a.id ASC")
                .build();
    }

    @Bean
    public ItemProcessor<Author, AuthorMongo> authorProcessor() {
        return authorConverter::fromJPAtoMongo;
    }

    @Bean
    public MongoItemWriter<AuthorMongo> authorWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<AuthorMongo>()
                .template(mongoTemplate)
                .collection("authors")
                .build();
    }
}
