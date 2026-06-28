package com.hanniel.ticketBookingSystem.dtos.event;

import java.util.UUID;

public record EventResponseDTO(
        UUID id,
        String name,
        String eventDate
) {}
