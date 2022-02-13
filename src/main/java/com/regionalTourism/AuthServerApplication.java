package com.regionalTourism;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AuthServerApplication {
	public static void main(String[] args) {
		//Para crear hash de password manualmente
		//BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		//System.out.println("RMR - passwordManual: "+passwordEncoder.encode("pocFal123"));

		SpringApplication.run(AuthServerApplication.class, args);
	}
}
