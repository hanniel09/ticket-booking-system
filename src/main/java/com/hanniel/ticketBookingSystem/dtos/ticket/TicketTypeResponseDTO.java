package com.hanniel.ticketBookingSystem.dtos.ticket;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Dados do tipo de ingresso retornado")
public record TicketTypeResponseDTO(
        @Schema(description = "Identificador único do tipo de ingresso", example = "b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e")
        UUID id,

        @Schema(description = "Identificador do evento associado", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
        UUID eventId,

        @Schema(description = "Nome ou categoria do ingresso", example = "VIP Open Bar")
        String name,

        @Schema(description = "Preço unitário", example = "250.00")
        BigDecimal price,

        @Schema(description = "Quantidade disponível no lote/estoque", example = "500")
        Long quantityAvailable
) {}
