package com.profesorp.capitalsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;

@SpringBootApplication
@EnableFeignClients("com.profesorp.capitalsservice")
public class CapitalsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapitalsServiceApplication.class, args);
	}
	@Bean
	public Sampler DefaultSampler()
	{
		return Sampler.ALWAYS_SAMPLE;
	}
}
