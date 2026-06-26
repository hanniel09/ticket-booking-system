package com.hanniel.ticketBookingSystem.dtos.ticket;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeResponseDTO(
        UUID id,
        UUID eventId,
        String name,
        BigDecimal price,
        Long quantityAvailable
) {}
