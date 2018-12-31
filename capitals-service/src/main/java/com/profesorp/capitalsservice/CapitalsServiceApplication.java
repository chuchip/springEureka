package com.profesorp.capitalsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.profesorp.capitalsservice")
// @EnableFeignClients 
public class CapitalsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapitalsServiceApplication.class, args);
	}
	
}
