package com.example.curse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.sql.*;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
		public class CurseApplication {
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(CurseApplication.class, args);
		Core core = new Core();
	}
}
