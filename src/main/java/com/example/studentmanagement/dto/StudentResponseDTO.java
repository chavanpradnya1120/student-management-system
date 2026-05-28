package com.example.studentmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String course;
    private int age;

}
