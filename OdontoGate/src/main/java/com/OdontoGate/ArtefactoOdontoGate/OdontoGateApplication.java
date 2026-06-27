package com.OdontoGate.ArtefactoOdontoGate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class OdontoGateApplication {

	public static void main(String[] args) {
		SpringApplication.run(OdontoGateApplication.class, args);
	}

}
