package com.example.joypadjourney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.example.joypadjourney.repository")
public class JoypadjourneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoypadjourneyApplication.class, args);
	}

}
