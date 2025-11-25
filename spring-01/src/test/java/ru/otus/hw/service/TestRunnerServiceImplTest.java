package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class TestRunnerServiceImplTest {
    @Mock
    private TestService testService;

    @Mock
    private IOService ioService;

    @InjectMocks
    private TestRunnerServiceImpl testRunnerService;

    @Test
    void run_executesTestServiceSuccessfully() {
        testRunnerService.run();

        verify(testService).executeTest();
        verifyNoMoreInteractions(ioService, testService);
    }

    @Test
    void run_handlesQuestionReadException() {
        doThrow(new QuestionReadException("Cannot read questions"))
                .when(testService).executeTest();

        testRunnerService.run();

        verify(ioService).printLine("Error reading questions");
        verify(testService).executeTest();
        verifyNoMoreInteractions(ioService, testService);
    }

    @Test
    void run_handlesUnexpectedException() {
        doThrow(new RuntimeException("Unexpected")).when(testService).executeTest();

        testRunnerService.run();

        verify(ioService).printLine("An unexpected error occurred");
        verify(testService).executeTest();
        verifyNoMoreInteractions(ioService, testService);
    }
}
