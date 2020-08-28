package com.dparadig.auth_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AuthServerApplication {
	public static void main(String[] args) {
		//Para crear hash de password manualmente
		//BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		//System.out.println("RMR - passwordManual: "+passwordEncoder.encode("pocFal123"));

		SpringApplication.run(AuthServerApplication.class, args);
	}
}
