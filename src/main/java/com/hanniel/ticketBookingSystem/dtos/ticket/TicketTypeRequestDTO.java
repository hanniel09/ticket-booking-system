package com.hanniel.ticketBookingSystem.dtos.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeRequestDTO(
        @NotNull(message = "Event ID is required")
        UUID eventId,

        @NotBlank(message = "Ticket type name is required")
        String name,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price cannot be negative")
        BigDecimal price,

        @NotNull(message = "Quantity available is required")
        @PositiveOrZero(message = "Quantity available cannot be negative")
        Long quantityAvailable
) {}
