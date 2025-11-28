package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.QuestionReadException;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestRunnerServiceImpl.class);

    private final TestService testService;

    private final IOService ioService;

    private final StudentService studentService;

    private final ResultService resultService;

    @Override
    public void run() {
        try {
            var student = studentService.determineCurrentStudent();
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } catch (QuestionReadException e) {
            LOGGER.error("Error reading questions", e);
            ioService.printLine("Error reading questions");
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred", e);
            ioService.printLine("An unexpected error occurred");
        }
    }
}
