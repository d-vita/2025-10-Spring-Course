package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.config.TestFileNameProvider;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CsvQuestionDao.class)
public class QuestionDaoIntegrationTest {
    @MockBean
    private TestFileNameProvider testFileNameProvider;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllShouldReadAllQuestionsFromCsv() {
        when(testFileNameProvider.getTestFileName()).thenReturn("test-questions.csv");

        List<Question> questions = csvQuestionDao.findAll();

        assertThat(questions)
                .hasSize(2)
                .extracting(Question::text)
                .containsExactly(
                        "What is 2 + 2?",
                        "What is the capital of France?"
                );

        Question first = questions.get(0);
        List<String> firstAnswers = first.answers().stream()
                .map(Answer::text)
                .collect(Collectors.toList());
        List<Boolean> firstCorrect = first.answers().stream()
                .map(Answer::isCorrect)
                .collect(Collectors.toList());

        assertThat(firstAnswers).containsExactlyInAnyOrder("22", "4");
        assertThat(firstCorrect).containsExactlyInAnyOrder(false, true);

        Question second = questions.get(1);
        List<String> secondAnswers = second.answers().stream()
                .map(Answer::text)
                .collect(Collectors.toList());
        List<Boolean> secondCorrect = second.answers().stream()
                .map(Answer::isCorrect)
                .collect(Collectors.toList());

        assertThat(secondAnswers).containsExactlyInAnyOrder("London", "Madrid", "Paris");
        assertThat(secondCorrect).containsExactlyInAnyOrder(false, false, true);
    }
}
