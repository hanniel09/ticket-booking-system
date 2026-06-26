package com.hanniel.ticketBookingSystem.controllers.auth;

import com.hanniel.ticketBookingSystem.dtos.auth.AuthenticationDTO;
import com.hanniel.ticketBookingSystem.dtos.auth.LoginResponseDTO;
import com.hanniel.ticketBookingSystem.dtos.auth.RegisterDTO;
import com.hanniel.ticketBookingSystem.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        log.info("Received login request for email: {}", data.email());
        LoginResponseDTO response = authService.login(data);
        log.info("Login successful for email: {}", data.email());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {
        log.info("Received registration request for email: {}", data.email());
        authService.register(data);
        log.info("Registration successful for email: {}", data.email());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
