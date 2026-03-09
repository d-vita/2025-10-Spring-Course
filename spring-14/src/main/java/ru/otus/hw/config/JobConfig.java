package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.listeners.JobLoggingAndCacheListener;


@RequiredArgsConstructor
@Configuration
public class JobConfig {

    private final JobRepository jobRepository;

    @Bean
    public Job importBookJob(Step bookStep, Step authorStep, Step genreStep,
                             JobLoggingAndCacheListener jobLoggingAndCacheListener) {
        return new JobBuilder("importBookJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(authorStep)
                .next(genreStep)
                .next(bookStep)
                .end()
                .listener(jobLoggingAndCacheListener)
                .build();
    }
}
