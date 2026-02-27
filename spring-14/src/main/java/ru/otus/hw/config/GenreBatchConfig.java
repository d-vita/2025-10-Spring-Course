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
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.jpa.Author;
import ru.otus.hw.models.jpa.Genre;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.GenreMongo;

@RequiredArgsConstructor
@Configuration
public class GenreBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final MongoTemplate mongoTemplate;
    private final GenreConverter genreConverter;

    private static final int CHUNK_SIZE = 5;

    @Bean
    public Job importGenreJob() {
        return new JobBuilder("importGenreJob", jobRepository)
                .start((genreStep()))
                .build();
    }

    @Bean
    public Step genreStep() {
        return new StepBuilder("genreStep", jobRepository)
                .<Genre, GenreMongo>chunk(CHUNK_SIZE, transactionManager)
                .reader(genreReader())
                .processor(genreProcessor())
                .writer(genreWriter(mongoTemplate))
                .build();
    }

    @Bean
    public JpaPagingItemReader<Genre> genreReader() {
        return new JpaPagingItemReaderBuilder<Genre>()
                .name("genreReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(10)
                .queryString("SELECT g FROM Genre g ORDER BY g.id ASC")
                .build();
    }

    @Bean
    public ItemProcessor<Genre, GenreMongo> genreProcessor() {
        return genreConverter::fromJPAtoMongo;
    }

    @Bean
    public MongoItemWriter<GenreMongo> genreWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<GenreMongo>()
                .template(mongoTemplate)
                .collection("genres")
                .build();
    }
}
