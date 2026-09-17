package com.hanniel.ticketBookingSystem.dtos.ticket;

import com.hanniel.ticketBookingSystem.domain.ticket.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Requisição para emissão ou atualização de ingresso")
public record TicketRequestDTO(
        @Schema(description = "Código único identificador do ingresso", example = "TCK-987654321")
        @NotBlank(message = "Ticket code is required")
        String ticketCode,

        @Schema(description = "Identificador do evento associado", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
        @NotNull(message = "Event ID is required")
        UUID eventId,

        @Schema(description = "Preço pago pelo ingresso", example = "175.00")
        @NotNull(message = "Price paid is required")
        @PositiveOrZero(message = "Price paid cannot be negative")
        BigDecimal pricePaid,

        @Schema(description = "Identificador do tipo de ingresso", example = "b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e")
        @NotNull(message = "Ticket type ID is required")
        UUID ticketTypeId,

        @Schema(description = "Status do ingresso", example = "AVAILABLE")
        @NotNull(message = "Status is required")
        TicketStatus status,

        @Schema(description = "Data e hora da compra", example = "2026-06-25T14:30:00Z")
        OffsetDateTime purchaseDate,

        @Schema(description = "Identificador do pedido ao qual o ingresso está vinculado", example = "d1e2f3a4-b5c6-7d8e-9f0a-1b2c3d4e5f6a")
        UUID orderId
) {}
