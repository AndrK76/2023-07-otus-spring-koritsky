package ru.otus.andrk;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMongock
@SpringBootApplication
public class Hw14BatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(Hw14BatchApplication.class, args);
	}

}
