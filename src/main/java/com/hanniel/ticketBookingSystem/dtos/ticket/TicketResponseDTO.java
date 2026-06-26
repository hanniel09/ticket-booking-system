package com.hanniel.ticketBookingSystem.dtos.ticket;

import com.hanniel.ticketBookingSystem.domain.ticket.TicketStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TicketResponseDTO(
        UUID id,
        String ticketCode,
        UUID eventId,
        BigDecimal pricePaid,
        UUID ticketTypeId,
        TicketStatus status,
        OffsetDateTime purchaseDate,
        UUID orderId
) {}
