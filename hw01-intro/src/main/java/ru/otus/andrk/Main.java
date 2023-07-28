package ru.otus.andrk;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.andrk.service.ContentService;

public class Main {
    public static void main(String[] args) {
        try (AbstractApplicationContext context =
                     new ClassPathXmlApplicationContext("/spring-context.xml")) {
            //TestContentDao testContentDao = context.getBean(TestContentDao.class);
            ContentService contentService = context.getBean(ContentService.class);
        }
    }
}