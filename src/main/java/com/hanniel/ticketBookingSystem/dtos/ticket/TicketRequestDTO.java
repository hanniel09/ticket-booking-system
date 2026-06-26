package com.hanniel.ticketBookingSystem.dtos.ticket;

import com.hanniel.ticketBookingSystem.domain.ticket.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TicketRequestDTO(
        @NotBlank(message = "Ticket code is required")
        String ticketCode,

        @NotNull(message = "Event ID is required")
        UUID eventId,

        @NotNull(message = "Price paid is required")
        @PositiveOrZero(message = "Price paid cannot be negative")
        BigDecimal pricePaid,

        @NotNull(message = "Ticket type ID is required")
        UUID ticketTypeId,

        @NotNull(message = "Status is required")
        TicketStatus status,

        OffsetDateTime purchaseDate,

        UUID orderId
) {}
