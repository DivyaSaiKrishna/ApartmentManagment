package com.example.ApartmentManagment;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com")
public class ApartmentManagmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApartmentManagmentApplication.class, args);
	}

}
