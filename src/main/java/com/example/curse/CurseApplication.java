package com.example.curse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
		public class CurseApplication {
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(CurseApplication.class, args);

		Core core = new Core();

		Properties prop = new Properties();
		String fileName = "src/main/resources/static/my_config.cfg";
		FileInputStream fis = null;
		int repeat = 0;
		long delay = 0;
		try {
			fis = new FileInputStream(fileName);
			prop.load(fis);
			delay = Long.valueOf(prop.getProperty("delay_time"));
			repeat = Integer.valueOf(prop.getProperty("repeat_times"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		while(repeat > 0) {
			core.start();
			Thread.sleep(delay);
			repeat--;
		}

	}
}
