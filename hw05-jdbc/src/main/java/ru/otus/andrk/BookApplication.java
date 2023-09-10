package ru.otus.andrk;

import lombok.extern.log4j.Log4j2;
import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class BookApplication {

    public static void main(String[] args) {
        try {
            Console.main(args);
        } catch (Exception e) {
            log.error(e);
        }
        SpringApplication.run(BookApplication.class, args);
    }

}
