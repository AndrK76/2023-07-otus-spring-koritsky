package ru.otus.andrk.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.otus.andrk.service.TroubleSource;

@RequiredArgsConstructor
@Component
public class TroubleAppRunner implements ApplicationRunner {

    private final TroubleSource troubleSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        troubleSource.startGenerateTroubles();
    }
}
