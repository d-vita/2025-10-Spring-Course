package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        printTestIntro();

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            ioService.printFormattedLine(formatQuestion(question));

            int answerIndex = readStudentAnswer(question);
            boolean isAnswerCorrect = isAnswerCorrect(question.answers(), answerIndex);

            testResult.applyAnswer(question, isAnswerCorrect);
        }

        return testResult;
    }

    private void printTestIntro() {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
    }

    private int readStudentAnswer(Question question) {
        int max = question.answers().size();
        return ioService.readIntForRange(
                1,
                max,
                ioService.getMessage("TestService.incorrect.input.error.message", max)
        );
    }

    private boolean isAnswerCorrect(List<Answer> answers, int answerIndex) {
        return answers.get(answerIndex - 1).isCorrect();
    }

    private String formatQuestion(Question question) {
        StringBuilder sb = new StringBuilder();
        sb.append(question.text()).append("\n");

        question.answers().forEach(answer ->
                sb.append("- ").append(answer.text()).append("\n")
        );

        return sb.toString().trim();
    }
}
