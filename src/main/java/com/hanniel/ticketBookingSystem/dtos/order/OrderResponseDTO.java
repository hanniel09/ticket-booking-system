package com.hanniel.ticketBookingSystem.dtos.order;

import com.hanniel.ticketBookingSystem.domain.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        UUID userId,
        UUID ticketTypeId,
        Integer quantity,
        BigDecimal totalAmount,
        OrderStatus status,
        UUID billingAddressId,
        OffsetDateTime createdAt
) {}
