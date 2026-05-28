package com.example.studentmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRequestDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Student full name", example = "Krushna11 Patil")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Student email address", example = "krushna@gmail.com")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Course selected by the student", example = "Advanced Computer")
    @NotBlank(message = "Course is required")
    private String course;

    @Schema(description = "Student age", example = "25")
    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 100, message = "Age must not be greater than 100")
    private int age;

}
