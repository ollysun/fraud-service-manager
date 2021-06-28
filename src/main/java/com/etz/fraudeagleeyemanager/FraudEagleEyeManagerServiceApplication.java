package com.etz.fraudeagleeyemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FraudEagleEyeManagerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FraudEagleEyeManagerServiceApplication.class, args);
	}

}
