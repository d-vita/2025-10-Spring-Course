package ru.otus.hw.commands;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@AllArgsConstructor
public class TestRunnerCommands {
    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Run test for student", key = "st")
    public void startTest () {
        testRunnerService.run();
    }
}
