package ru.otus.andrk;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.Console;

@EnableMongock
@SpringBootApplication
public class BatchApp {

	public static void main(String[] args) {
		SpringApplication.run(BatchApp.class, args);
	}

}
