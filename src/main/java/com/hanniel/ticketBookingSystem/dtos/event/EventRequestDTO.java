package com.hanniel.ticketBookingSystem.dtos.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requisição para criação ou atualização de evento")
public record EventRequestDTO(
        @Schema(description = "Nome do evento", example = "Rock in Rio 2026")
        @NotBlank(message = "O nome do evento é obrigatório")
        String name,

        @Schema(description = "Data do evento no formato DD/MM/YYYY", example = "27/06/2026")
        @NotBlank(message = "A data do evento é obrigatória")
        String eventDate
) {}
