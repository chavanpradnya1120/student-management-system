package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.*;
import com.example.studentmanagement.security.JwtUtil;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final JwtUtil jwtUtil;

    @Override
    public String signup(SignupRequestDTO signupRequestDTO) {

        if(userRepository.existsByEmail(signupRequestDTO.getEmail())){
            throw new RuntimeException("Email already exists");

        }

        User user=new User();
        user.setName(signupRequestDTO.getName());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        user.setRole("USER");
        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Optional<User> optionalUser=userRepository.findByEmail(loginRequestDTO.getEmail());

        if(optionalUser.isEmpty()){

            throw new RuntimeException("Invalid email or password");

        }

        User user=optionalUser.get();

        boolean isMatchPassword=passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());
        String accessToken = jwtUtil.generateAccessToken(user.getEmail());

        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return new LoginResponseDTO(
                "Login successful",
                accessToken,
                refreshToken
        );


        //return new LoginResponseDTO("Login successful",jwtUtil.generateToken(loginRequestDTO.getEmail()));
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO requestDTO) {
        String refreshToken = requestDTO.getRefreshToken();

        String email = jwtUtil.extractEmail(refreshToken);

        User user1 = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtUtil.validateToken(refreshToken, user1.getEmail())) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user1.getEmail());

        return new RefreshTokenResponseDTO(newAccessToken);
    }
}
