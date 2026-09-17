package com.hanniel.ticketBookingSystem.dtos.order;

import com.hanniel.ticketBookingSystem.domain.order.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Dados do pedido retornado")
public record OrderResponseDTO(
        @Schema(description = "Identificador único do pedido", example = "d1e2f3a4-b5c6-7d8e-9f0a-1b2c3d4e5f6a")
        UUID id,

        @Schema(description = "Identificador do usuário que realizou o pedido", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
        UUID userId,

        @Schema(description = "Identificador do tipo de ingresso", example = "b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e")
        UUID ticketTypeId,

        @Schema(description = "Quantidade de ingressos adquiridos", example = "2")
        Integer quantity,

        @Schema(description = "Valor total do pedido", example = "350.00")
        BigDecimal totalAmount,

        @Schema(description = "Status atual do pedido", example = "PENDING")
        OrderStatus status,

        @Schema(description = "Identificador do endereço fiscal vinculado", example = "c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f")
        UUID billingAddressId,

        @Schema(description = "Data e hora de criação do pedido", example = "2026-06-25T14:30:00Z")
        OffsetDateTime createdAt
) {}
