package com.hanniel.ticketBookingSystem.dtos.event;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EventResponseDTO(
        UUID id,
        String name,
        OffsetDateTime eventDate
) {}
