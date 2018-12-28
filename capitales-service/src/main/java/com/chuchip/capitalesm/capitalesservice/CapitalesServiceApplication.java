package com.chuchip.capitalesm.capitalesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;

@SpringBootApplication
@EnableFeignClients("com.chuchip.capitalesm.capitalesservice")
public class CapitalesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapitalesServiceApplication.class, args);
	}
	@Bean
	public Sampler DefaultSampler()
	{
		return Sampler.ALWAYS_SAMPLE;
	}
}
