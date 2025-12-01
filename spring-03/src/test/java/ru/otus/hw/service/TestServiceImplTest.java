package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    @Test
    void executeTest_printsAllQuestionsAndAnswers() {

        Question q1 = new Question(
                "Is there life on Mars?",
                List.of(
                        new Answer("Science doesn't know this yet", false),
                        new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false),
                        new Answer("Absolutely not", true)
                )
        );

        Question q2 = new Question(
                "How should resources be loaded form jar in Java?",
                List.of(
                        new Answer("ClassLoader#getResourceAsStream or ClassPathResource#getInputStream", true),
                        new Answer("ClassLoader#getResource#getFile + FileReader", false),
                        new Answer("Wingardium Leviosa", false)
                )
        );

        when(questionDao.findAll()).thenReturn(List.of(q1, q2));

        when(ioService.readIntForRange(eq(1), eq(3), anyString()))
                .thenReturn(3)
                .thenReturn(1);

        Student student = new Student("Ivan", "Petrov");

        testService.executeTestFor(student);

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");

        verify(ioService).printLine(
                """
                Is there life on Mars?
                - Science doesn't know this yet
                - Certainly. The red UFO is from Mars. And green is from Venus
                - Absolutely not"""
        );

        verify(ioService).printLine(
                """
                How should resources be loaded form jar in Java?
                - ClassLoader#getResourceAsStream or ClassPathResource#getInputStream
                - ClassLoader#getResource#getFile + FileReader
                - Wingardium Leviosa"""
        );

        verify(ioService, times(2)).readIntForRange(eq(1), eq(3), anyString());

        verifyNoMoreInteractions(ioService);
    }
}
