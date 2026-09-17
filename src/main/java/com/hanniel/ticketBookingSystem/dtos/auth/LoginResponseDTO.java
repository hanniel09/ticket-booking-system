package com.hanniel.ticketBookingSystem.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Resposta de autenticação com token JWT")
public record LoginResponseDTO(
        @Schema(description = "Token JWT emitido para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        @NotBlank
        String token
) {}
