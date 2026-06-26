package com.hanniel.ticketBookingSystem.dtos.order;

import com.hanniel.ticketBookingSystem.domain.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderRequestDTO(
        @NotNull(message = "User ID is required")
        UUID userId,

        @NotNull(message = "Ticket type ID is required")
        UUID ticketTypeId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity,

        @NotNull(message = "Total amount is required")
        @PositiveOrZero(message = "Total amount cannot be negative")
        BigDecimal totalAmount,

        @NotNull(message = "Order status is required")
        OrderStatus status,

        UUID billingAddressId
) {}
