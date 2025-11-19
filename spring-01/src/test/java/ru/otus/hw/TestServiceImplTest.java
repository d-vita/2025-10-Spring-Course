package ru.otus.hw;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

import static org.mockito.Mockito.*;

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

        testService.executeTest();

        verify(ioService).printLine("");
        verify(ioService).printLine("Please answer the questions below");

        verify(ioService).printLine(q1.text());
        q1.answers().forEach(a ->
                verify(ioService).printFormattedLine("- %s", a.text())
        );

        verify(ioService).printLine(q2.text());
        q2.answers().forEach(a ->
                verify(ioService).printFormattedLine("- %s", a.text())
        );

        verifyNoMoreInteractions(ioService);
    }
}
