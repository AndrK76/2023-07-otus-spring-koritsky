package ru.otus.andrk;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.andrk.service.TestSystemService;

public class Main {
    public static void main(String[] args) {
        try (AbstractApplicationContext context =
                     new ClassPathXmlApplicationContext("/spring-context.xml")) {
            TestSystemService testSystem = context.getBean(TestSystemService.class);
            testSystem.runTest();
        }
    }
}