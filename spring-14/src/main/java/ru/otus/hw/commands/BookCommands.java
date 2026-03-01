package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;


@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final Job importBookJob;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "mig-b")
    public void migrateAllGenres() throws Exception {
        JobExecution execution = jobLauncher.run(
                importBookJob,
                new JobParametersBuilder()
                        .addLong("run.id", System.currentTimeMillis())
                        .toJobParameters()
        );
        System.out.println(execution);
    }
}
