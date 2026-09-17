package com.hanniel.ticketBookingSystem.dtos.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Requisição para criação ou atualização de tipo de ingresso")
public record TicketTypeRequestDTO(
        @Schema(description = "Identificador do evento ao qual o tipo de ingresso pertence", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
        @NotNull(message = "Event ID is required")
        UUID eventId,

        @Schema(description = "Nome ou categoria do ingresso (ex: Pista, VIP, Camarote)", example = "VIP Open Bar")
        @NotBlank(message = "Ticket type name is required")
        String name,

        @Schema(description = "Preço unitário do ingresso", example = "250.00")
        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price cannot be negative")
        BigDecimal price,

        @Schema(description = "Quantidade disponível no lote/estoque", example = "500")
        @NotNull(message = "Quantity available is required")
        @PositiveOrZero(message = "Quantity available cannot be negative")
        Long quantityAvailable
) {}
