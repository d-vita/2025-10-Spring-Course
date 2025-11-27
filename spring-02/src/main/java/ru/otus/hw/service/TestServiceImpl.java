package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printLine("Please answer the questions below");

        printAllQuestions();
    }

    private void printAllQuestions() {
        for (Question question : questionDao.findAll()) {
            ioService.printLine(question.text());
            question.answers().forEach(answer -> ioService.printLine(formatAnswer(answer)));
        }
    }

    private String formatAnswer(Answer answer) {
        return String.format("- %s", answer.text());
    }
}
