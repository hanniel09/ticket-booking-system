package com.hanniel.ticketBookingSystem.dtos.event;

import jakarta.validation.constraints.NotBlank;

public record EventRequestDTO(
        @NotBlank(message = "Event name is required")
        String name,

        @NotBlank(message = "Event date is required")
        String eventDate
) {}
