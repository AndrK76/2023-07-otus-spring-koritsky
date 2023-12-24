package ru.otus.andrk;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMongock
@SpringBootApplication
public class BatchApp {

    //java -Dspring.profiles.active=docker -jar batch-converter.jar
    //--spring.shell.interactive.enabled=false --spring.batch.job.enabled=true
    //--spring.shell.interactive.enabled=false --spring.batch.job.enabled=true clear=false

    public static void main(String[] args) {
        SpringApplication.run(BatchApp.class, args);
    }

}
