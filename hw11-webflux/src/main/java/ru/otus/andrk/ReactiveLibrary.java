package ru.otus.andrk;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableMongock
public class ReactiveLibrary {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveLibrary.class, args);
		System.out.println("\n\nhttp://localhost:8080");
	}

}
