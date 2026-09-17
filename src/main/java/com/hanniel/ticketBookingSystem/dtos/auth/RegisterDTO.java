package com.hanniel.ticketBookingSystem.dtos.auth;

import com.hanniel.ticketBookingSystem.domain.user.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de novo usuário")
public record RegisterDTO(
        @Schema(description = "Endereço de email", example = "novo.usuario@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "Senha de acesso", example = "senhaForte123")
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password,

        @Schema(description = "Papel/Perfil de acesso do usuário", example = "USER")
        UserRole role
) {}
