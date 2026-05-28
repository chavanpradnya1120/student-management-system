package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.*;

public interface AuthService {

    String signup(SignupRequestDTO signupRequestDTO);
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO requestDTO);
}
