package com.example.studentmanagement;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class StudentmanagementApplication {

	public static void main(String[] args) {


		SpringApplication.run(StudentmanagementApplication.class, args);


	}

	@PostConstruct
	public void init() {
		new File("uploads/student-images").mkdirs();
		new File("logs").mkdirs();
	}
}
