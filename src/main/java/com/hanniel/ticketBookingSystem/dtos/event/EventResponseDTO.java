package com.hanniel.ticketBookingSystem.dtos.event;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados do evento retornado")
public record EventResponseDTO(
        @Schema(description = "Identificador único do evento", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
        UUID id,

        @Schema(description = "Nome do evento", example = "Rock in Rio 2026")
        String name,

        @Schema(description = "Data do evento no formato DD/MM/YYYY", example = "27/06/2026")
        String eventDate
) {}
