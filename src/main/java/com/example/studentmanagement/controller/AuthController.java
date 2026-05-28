package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.*;
import com.example.studentmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO){

        String response= authService.signup(signupRequestDTO);


        return  ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){

        LoginResponseDTO responseDTO=authService.login(loginRequestDTO);

        return ResponseEntity.ok(responseDTO);

    }

    @PostMapping("/refresh")
    public RefreshTokenResponseDTO refreshToken(
            @RequestBody RefreshTokenRequestDTO requestDTO
    ) {
        return authService.refreshToken(requestDTO);
    }

}
