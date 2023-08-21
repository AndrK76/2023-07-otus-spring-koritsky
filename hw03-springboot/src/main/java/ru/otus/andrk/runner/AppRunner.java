package ru.otus.andrk.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.otus.andrk.service.TestSystemService;

@Component
public class AppRunner implements ApplicationRunner {

    private final TestSystemService testSystemService;

    public AppRunner(
            @Autowired TestSystemService testSystemService) {
        this.testSystemService = testSystemService;
    }

    @Override
    public void run(ApplicationArguments args) {
        testSystemService.runTest();
    }
}
