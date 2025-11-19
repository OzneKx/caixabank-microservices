package com.caixabank.antifraud_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AntifraudServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AntifraudServiceApplication.class, args);
	}

}
