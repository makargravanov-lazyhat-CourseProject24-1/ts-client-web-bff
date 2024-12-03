package ru.jetlabs.ts.clientwebbff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan("ru.jetlabs.ts.clientwebbff.config")
public class TourSystemBffApplication {
	public static void main(String[] args) {
		SpringApplication.run(TourSystemBffApplication.class, args);
	}
}

