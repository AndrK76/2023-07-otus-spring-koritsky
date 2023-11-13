package ru.otus.andrk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookServerAuth {

	public static void main(String[] args) {
		SpringApplication.run(BookServerAuth.class, args);
		System.out.println("http://localhost:8080");
	}

}
