package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
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
import ru.otus.hw.models.jpa.Genre;
import ru.otus.hw.models.mongo.GenreMongo;
import ru.otus.hw.services.BookMigrationService;

@RequiredArgsConstructor
@Configuration
public class GenreStepConfig {

    private final JobProperties batchProperties;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    private final EntityManagerFactory entityManagerFactory;

    private final MongoTemplate mongoTemplate;

    private final GenreConverter genreConverter;

    private final BookMigrationService bookMigrationService;

    @Bean
    public Step genreStep() {
        return new StepBuilder("genreStep", jobRepository)
                .<Genre, GenreMongo>chunk(batchProperties.getChunkSize(), transactionManager)
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
                .pageSize(batchProperties.getPageSize())
                .queryString("SELECT g FROM Genre g ORDER BY g.id ASC")
                .build();
    }

    @Bean
    public ItemProcessor<Genre, GenreMongo> genreProcessor() {
        return genre -> {
            GenreMongo mongo = genreConverter.fromJPAtoMongo(genre);
            bookMigrationService.putGenre(genre.getId(), mongo);
            return mongo;
        };
    }

    @Bean
    public MongoItemWriter<GenreMongo> genreWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<GenreMongo>()
                .template(mongoTemplate)
                .collection("genres")
                .build();
    }
}
