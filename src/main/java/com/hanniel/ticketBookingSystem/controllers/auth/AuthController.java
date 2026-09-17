package com.hanniel.ticketBookingSystem.controllers.auth;

import com.hanniel.ticketBookingSystem.dtos.auth.AuthenticationDTO;
import com.hanniel.ticketBookingSystem.dtos.auth.LoginResponseDTO;
import com.hanniel.ticketBookingSystem.dtos.auth.RegisterDTO;
import com.hanniel.ticketBookingSystem.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticação", description = "Endpoints de registro e autenticação de usuários")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Realizar login", description = "Autentica o usuário com email e senha, retornando um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        log.info("Received login request for email: {}", data.email());
        LoginResponseDTO response = authService.login(data);
        log.info("Login successful for email: {}", data.email());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar novo usuário", description = "Cadastra um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos ou usuário já existente")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {
        log.info("Received registration request for email: {}", data.email());
        authService.register(data);
        log.info("Registration successful for email: {}", data.email());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
