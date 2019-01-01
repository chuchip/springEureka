package com.profesorp.capitalsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients 
@RibbonClient(name="countries-service", configuration = RibbonConfiguration.class)
public class CapitalsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapitalsServiceApplication.class, args);
	}
	
}
