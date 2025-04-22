package com.testportal.online_test_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class OnlineTestPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineTestPortalApplication.class, args);
	}

}
