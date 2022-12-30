package com.differentdoors.microsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class MicrosoftApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrosoftApplication.class, args);
	}

}
