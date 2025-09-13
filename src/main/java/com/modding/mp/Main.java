package com.modding.mp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.modding.mp.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		
	}

}
