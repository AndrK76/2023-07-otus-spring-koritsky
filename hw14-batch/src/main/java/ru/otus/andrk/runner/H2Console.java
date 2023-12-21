package ru.otus.andrk.runner;

import org.h2.tools.Console;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(-1)
@ConditionalOnProperty(value = "spring.h2.console.enabled", havingValue = "true")
public class H2Console implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Console.main(args.getSourceArgs());
    }
}
