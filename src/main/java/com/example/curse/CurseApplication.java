package com.example.curse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurseApplication.class, args);
		Core core = new Core();
	}

}