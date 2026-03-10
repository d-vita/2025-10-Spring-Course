package ru.otus.hw.listeners;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.BookMigrationService;

@Component
@RequiredArgsConstructor
public class JobLoggingAndCacheListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobLoggingAndCacheListener.class);

    private final BookMigrationService bookMigrationService;

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("Job '{}' started at {}", jobExecution.getJobInstance().getJobName(),
                jobExecution.getStartTime());
        bookMigrationService.clearCache();
        LOGGER.info("Clearing cache before job completed");
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        LOGGER.info("Job '{}' finished with status {}", jobExecution.getJobInstance().getJobName(),
                jobExecution.getExitStatus());
    }
}
