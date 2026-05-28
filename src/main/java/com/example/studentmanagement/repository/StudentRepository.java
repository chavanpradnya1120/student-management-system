package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Student;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {


    boolean existsByEmail(@Email(message = "Invalid email format") String email);

    List<Student> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourseContainingIgnoreCase(
            String name,
            String email,
            String course
    );

    List<Student> findByDeletedFalseAndNameContainingIgnoreCaseOrDeletedFalseAndEmailContainingIgnoreCaseOrDeletedFalseAndCourseContainingIgnoreCase(
            String name,
            String email,
            String course
    );

    List<Student> findByCourseIgnoreCaseAndAge(String course, int age);
    List<Student> findByCourseIgnoreCase(String course);

    List<Student> findByAge(int age);

    List<Student> findByDeletedFalseAndCourseIgnoreCase(String course);

    List<Student> findByDeletedFalseAndAge(int age);

    List<Student> findByDeletedFalseAndCourseIgnoreCaseAndAge(String course, int age);

    List<Student> findByDeletedFalse();

    Optional<Student> findByIdAndDeletedFalse(Long id);
    Page<Student> findByDeletedFalse(Pageable pageable);


}