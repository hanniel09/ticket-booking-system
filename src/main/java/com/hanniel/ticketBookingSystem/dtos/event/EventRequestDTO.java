package com.hanniel.ticketBookingSystem.dtos.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record EventRequestDTO(
        @NotBlank(message = "Event name is required")
        String name,

        @NotNull(message = "Event date is required")
        OffsetDateTime eventDate
) {}
