package com.example.studentmanagement.controller;


import com.example.studentmanagement.dto.StudentRequestDTO;
import com.example.studentmanagement.dto.StudentResponseDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public StudentController(StudentService studentService,
                             StudentRepository studentRepository){
        this.studentService=studentService;
        this.studentRepository = studentRepository;
    }
    //create student
    @Operation(
            summary = "Create a new student",
            description = "This API is used to add a new student to the system."
    )
   // @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public StudentResponseDTO createStudent(@RequestBody @Valid StudentRequestDTO student){
        return studentService.saveStudent(student);

    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/all")
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> getstudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam (defaultValue = "5")int size,
            @RequestParam (defaultValue = "id")String sortBy,
            @RequestParam (defaultValue = "asc")String direction
    ){

        return ResponseEntity.ok(studentService.getStudents(page,size,sortBy,direction));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id){
        return studentService.getStudentById(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id){

        studentService.deleteStudent(id);
        return "Student deleted successfully";
    }

    @Operation(
            summary = "Update student details",
            description = "This API updates student information by student ID."
    )
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Student updated successfully"),

            @ApiResponse(responseCode = "404", description = "Student not found"),

            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id,@RequestBody @Valid Student student){
        return studentService.updateStudent(id,student);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(
            @RequestParam String keyword
    ) {
        List<StudentResponseDTO> students = studentService.searchStudents(keyword);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<StudentResponseDTO>> filterStudents(
            @RequestParam(required = false) String course,
            @RequestParam(required = false) Integer age
    ) {
        List<StudentResponseDTO> students = studentService.filterStudents(course, age);
        return ResponseEntity.ok(students);
    }


    @PostMapping("{id}/upload-image")
    public ResponseEntity<String> uploadStudentImage(
            @PathVariable Long id,
            @RequestParam("file")MultipartFile file){

        String msg=studentService.uploadStudentImage(id,file);
        return ResponseEntity.ok(msg);

    }


    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {

        try {

            Path imagePath = Paths.get(uploadDir).resolve(imageName);

            // If image not found → return default image
            if (!Files.exists(imagePath)) {
                imagePath = Paths.get(uploadDir).resolve("default.png");
            }

            Resource resource = new UrlResource(imagePath.toUri());

            String contentType = Files.probeContentType(imagePath);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {

            throw new RuntimeException("Failed to load image: " + e.getMessage());
        }
    }


    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportStudentsToPdf(){

        byte[] pdfdata=studentService.exportStudentsToPdf();

        HttpHeaders headers=new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachement","students.pdf");

        return new ResponseEntity<>(pdfdata,headers, HttpStatus.OK);



    }




}
