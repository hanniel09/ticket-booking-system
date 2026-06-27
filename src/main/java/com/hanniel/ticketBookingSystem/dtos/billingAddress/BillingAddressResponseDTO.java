package com.hanniel.ticketBookingSystem.dtos.billingAddress;

import java.util.UUID;

public record BillingAddressResponseDTO(
        UUID id,
        UUID userId,
        String name,
        String taxId,
        String postalCode,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String uf,
        String phone,
        String email,
        Boolean shipping
) {}
