package ru.otus.andrk.service;

import ru.otus.andrk.dao.QuestionDao;

public class TestSystemServiceImpl implements TestSystemService {
    private final QuestionDao questionDao;

    private final ConverterService converterService;

    private final DialogService dialogService;

    public TestSystemServiceImpl(
            QuestionDao questionDao,
            ConverterService converterService,
            DialogService dialogService) {
        this.questionDao = questionDao;
        this.converterService = converterService;
        this.dialogService = dialogService;
    }

    @Override
    public void runTest() {
        for (var question : questionDao.getQuestions()) {
            dialogService.displayText(converterService.queryToString(question));
        }
    }
}
