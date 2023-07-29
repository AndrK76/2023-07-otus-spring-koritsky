package ru.otus.andrk.service;

public class TestSystemServiceImpl implements TestSystemService {
    private final ContentService contentService;

    private final DialogService dialogService;

    public TestSystemServiceImpl(ContentService contentService, DialogService dialogService) {
        this.contentService = contentService;
        this.dialogService = dialogService;
    }

    @Override
    public void runTest() {
        for (var question : contentService.getQuestions()) {
            dialogService.showQuestionWithAnswers(question);
        }
    }
}
