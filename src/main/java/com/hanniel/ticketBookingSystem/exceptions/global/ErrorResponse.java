package com.hanniel.ticketBookingSystem.exceptions.global;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Estrutura padronizada de resposta de erro da API")
public record ErrorResponse(
        @Schema(description = "Código de status HTTP", example = "400")
        int status,

        @Schema(description = "Mensagem descritiva do erro", example = "Recurso não encontrado")
        String message,

        @Schema(description = "Carimbo de data e hora do erro", example = "2026-06-25T14:30:00")
        LocalDateTime timestamp
) {}
