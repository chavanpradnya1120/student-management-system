package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequestDTO;
import com.example.studentmanagement.dto.StudentResponseDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.BadRequestException;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.util.StudentPdfExporter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;



    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final Logger logger=LoggerFactory.getLogger(StudentServiceImpl.class);



    @Override
    public StudentResponseDTO saveStudent(StudentRequestDTO dto) {
        // 🔴 Convert DTO → Entity
        Student student = modelMapper.map(dto, Student.class);


        // business validation
        if (studentRepository.existsByEmail(student.getEmail())) {

            logger.warn("Student creation failed. Email already exists: {}", student.getEmail());
            throw new BadRequestException("Email already exists");
        }

        if (student.getAge() < 18) {

            logger.warn("Student creation failed due to invalid age: {}", student.getAge());
            throw new BadRequestException("Student must be adult");
        }

        logger.info("Creating student with name: {}", dto.getName());
        Student saved = studentRepository.save(student);

        // 🟢 Convert Entity → DTO
        StudentResponseDTO response = modelMapper.map(saved, StudentResponseDTO.class);

        logger.info("Student saved successfully with id: {}", saved.getId());

        return response;
    }

    @Override
    public List<Student> getAllStudents() {

        List<Student> students = studentRepository.findByDeletedFalse();

        return students;
    }

    @Override
    public Page<StudentResponseDTO> getStudents(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

      //  Page<Student> studentPage = studentRepository.findAll(pageable);
        Page<Student> studentPage =
                studentRepository.findByDeletedFalse(pageable);


        return studentPage.map(student -> modelMapper.map(student, StudentResponseDTO.class));
    }


    @Override
    public Student getStudentById(Long id) {

        logger.info("Fetching student with id: {}", id);

        return studentRepository.findByIdAndDeletedFalse(id).orElseThrow(
                () -> {
                        logger.warn("Student not found with id: {}", id);
                       return new ResourceNotFoundException("student not found");
                }
        );
    }

    @Override
    public void deleteStudent(Long id) {

        Student student = studentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        student.setDeleted(true);

        logger.warn("Deleting student with id: {}", id);

        studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        Student existingStudent = studentRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("student not found"));
        logger.info("Updating student with id: {}", id);
        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setCourse(student.getCourse());
        existingStudent.setAge(student.getAge());
        existingStudent.setDeleted(student.isDeleted());

        logger.info("Student updated successfully with id: {}", existingStudent.getId());


        return studentRepository.save(existingStudent);
    }

    @Override
    public List<StudentResponseDTO> searchStudents(String keyword) {

//        List<Student> students =
//                studentRepository
//                        .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourseContainingIgnoreCase(
//                                keyword,
//                                keyword,
//                                keyword
//                        );
        List<Student> students =
                studentRepository
                        .findByDeletedFalseAndNameContainingIgnoreCaseOrDeletedFalseAndEmailContainingIgnoreCaseOrDeletedFalseAndCourseContainingIgnoreCase(
                                keyword,
                                keyword,
                                keyword
                        );

        return students.stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .toList();
    }

    @Override
    public List<StudentResponseDTO> filterStudents(String course, Integer age) {

        List<Student> students;

        if (course != null && age != null) {

            students = studentRepository
                    .findByDeletedFalseAndCourseIgnoreCaseAndAge(course, age);

        } else if (course != null) {

            students = studentRepository
                    .findByDeletedFalseAndCourseIgnoreCase(course);

        } else if (age != null) {

            students = studentRepository
                    .findByDeletedFalseAndAge(age);

        } else {

            students = studentRepository.findByDeletedFalse();
        }

        return students.stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .toList();
    }


    @Override
    public String uploadStudentImage(Long id, MultipartFile file) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id " + id));

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !(contentType.equals("image/jpeg")
                        || contentType.equals("image/png")
                        || contentType.equals("image/jpg"))) {

            throw new RuntimeException("Only JPG, JPEG, PNG images are allowed");
        }

        try {

            File folder = new File(uploadDir);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null || !originalFileName.contains(".")) {
                throw new RuntimeException("Invalid file name");
            }

            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // Unique image name
            String uniqueFileName = UUID.randomUUID().toString() + extension;

            Path filePath = Paths.get(uploadDir).resolve(uniqueFileName);

            logger.info("Uploading image for student id: {}", id);

            // Save image
            Files.copy(file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            // Delete old image if not default
            String oldImage = student.getProfileImage();

            if (oldImage != null
                    && !oldImage.isBlank()
                    && !oldImage.equals("default.png")) {

                Path oldImagePath = Paths.get(uploadDir).resolve(oldImage);

                Files.deleteIfExists(oldImagePath);
            }

            // Save new image name in DB
            student.setProfileImage(uniqueFileName);

            studentRepository.save(student);

            logger.info("Image uploaded successfully for student id: {}", id);

            return "Image uploaded successfully: " + uniqueFileName;

        } catch (Exception e) {

            logger.error("Failed to upload image for student id: {}", id, e);

            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }






    @Override
    public byte[] exportStudentsToPdf() {

        logger.info("Exporting students data to PDF");
        List<Student> students=studentRepository.findByDeletedFalse();

        return StudentPdfExporter.export(students);

    }
}
