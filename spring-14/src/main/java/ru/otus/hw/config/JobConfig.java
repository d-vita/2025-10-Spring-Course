package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@RequiredArgsConstructor
@Configuration
public class JobConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobConfig.class);


    private final JobRepository jobRepository;



    @Bean
    public Job importBookJob(Step bookStep, Step authorStep, Step genreStep) {
        return new JobBuilder("importBookJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(authorStep)
                .next(genreStep)
                .next(bookStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        LOGGER.info(
                                "Job '{}' started at {}", jobExecution.getJobInstance().getJobName(),
                                jobExecution.getStartTime());
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        LOGGER.info(
                                "Job '{}' finished with status {}", jobExecution.getJobInstance().getJobName(),
                                jobExecution.getExitStatus());
                    }
                })
                .build();
    }
}
