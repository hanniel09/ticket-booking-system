package com.hanniel.ticketBookingSystem.dtos.ticket;

import com.hanniel.ticketBookingSystem.domain.ticket.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Dados do ingresso retornado")
public record TicketResponseDTO(
        @Schema(description = "Identificador único do ingresso", example = "e3f4a5b6-c7d8-9e0f-1a2b-3c4d5e6f7a8b")
        UUID id,

        @Schema(description = "Código único legível do ingresso", example = "TCK-987654321")
        String ticketCode,

        @Schema(description = "Identificador do evento associado", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
        UUID eventId,

        @Schema(description = "Preço pago pelo ingresso", example = "175.00")
        BigDecimal pricePaid,

        @Schema(description = "Identificador do tipo de ingresso", example = "b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e")
        UUID ticketTypeId,

        @Schema(description = "Status atual do ingresso", example = "AVAILABLE")
        TicketStatus status,

        @Schema(description = "Data e hora de emissão/compra", example = "2026-06-25T14:30:00Z")
        OffsetDateTime purchaseDate,

        @Schema(description = "Identificador do pedido ao qual o ingresso pertence", example = "d1e2f3a4-b5c6-7d8e-9f0a-1b2c3d4e5f6a")
        UUID orderId
) {}
