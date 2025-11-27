package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestRunnerServiceImpl.class);

    private final TestService testService;

    private final IOService ioService;

    @Override
    public void run() {
        try {
            testService.executeTest();
        } catch (QuestionReadException e) {
            LOGGER.error("Error reading questions", e);
            ioService.printLine("Error reading questions");
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred", e);
            ioService.printLine("An unexpected error occurred");
        }
    }
}
