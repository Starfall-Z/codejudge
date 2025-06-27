package com.example.codejudge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CodejudgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodejudgeApplication.class, args);
	}

}
