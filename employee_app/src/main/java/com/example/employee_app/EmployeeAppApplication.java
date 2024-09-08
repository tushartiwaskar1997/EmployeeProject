package com.example.employee_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmployeeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeAppApplication.class, args);
		System.out.println("Applicationn started successfully ");
	}

}
