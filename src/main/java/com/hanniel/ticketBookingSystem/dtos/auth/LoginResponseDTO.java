package com.hanniel.ticketBookingSystem.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDTO(
        @NotBlank
        String token
) {}
