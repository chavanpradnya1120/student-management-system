package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequestDTO;
import com.example.studentmanagement.dto.StudentResponseDTO;
import com.example.studentmanagement.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface StudentService {

    StudentResponseDTO saveStudent(StudentRequestDTO student);
    List<Student> getAllStudents();
    Page<StudentResponseDTO> getStudents(int page,int size,String sortBy,String direction);
    Student getStudentById(Long id);
    void deleteStudent(Long id);
    Student updateStudent(Long id,Student student);
    List<StudentResponseDTO> searchStudents(String keyword);
    List<StudentResponseDTO> filterStudents(String course, Integer age);
    String uploadStudentImage(Long id, MultipartFile file);
    byte[] exportStudentsToPdf();

}
