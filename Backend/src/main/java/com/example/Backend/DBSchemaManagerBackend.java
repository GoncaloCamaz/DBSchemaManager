package com.example.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.Backend.model"})
public class DBSchemaManagerBackend {

	public static void main(String[] args) {
		SpringApplication.run(DBSchemaManagerBackend.class, args);
	}
}
