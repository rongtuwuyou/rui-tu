package com.daowuyou.sessionservice;

import com.daowuyou.api.config.FeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.daowuyou.api.client", defaultConfiguration= FeignConfig.class)
@SpringBootApplication
public class SessionServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(SessionServiceApplication.class, args);
	}
}