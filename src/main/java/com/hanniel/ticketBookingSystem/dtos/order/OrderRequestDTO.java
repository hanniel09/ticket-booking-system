package com.hanniel.ticketBookingSystem.dtos.order;

import com.hanniel.ticketBookingSystem.domain.order.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Requisição para criação ou atualização de pedido")
public record OrderRequestDTO(
        @Schema(description = "Identificador do usuário que realizou o pedido", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
        @NotNull(message = "User ID is required")
        UUID userId,

        @Schema(description = "Identificador do tipo de ingresso desejado", example = "b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e")
        @NotNull(message = "Ticket type ID is required")
        UUID ticketTypeId,

        @Schema(description = "Quantidade de ingressos no pedido", example = "2")
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity,

        @Schema(description = "Valor total do pedido", example = "350.00")
        @NotNull(message = "Total amount is required")
        @PositiveOrZero(message = "Total amount cannot be negative")
        BigDecimal totalAmount,

        @Schema(description = "Status atual do pedido", example = "PENDING")
        @NotNull(message = "Order status is required")
        OrderStatus status,

        @Schema(description = "Identificador do endereço fiscal associado ao pedido", example = "c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f")
        UUID billingAddressId
) {}
