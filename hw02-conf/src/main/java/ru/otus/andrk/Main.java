package ru.otus.andrk;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractApplicationContext;
import ru.otus.andrk.service.TestSystemService;

@ComponentScan
@PropertySource("classpath:application.properties")
public class Main {
    public static void main(String[] args) {
        try (AbstractApplicationContext context =
                     new AnnotationConfigApplicationContext(Main.class)) {
            TestSystemService testSystem = context.getBean(TestSystemService.class);
            testSystem.runTest();
        }
    }
}