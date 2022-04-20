package com.joke_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JokeProviderApplication {

	public static void main(String[] args) {

		SpringApplication.run(JokeProviderApplication.class, args);
	}

}