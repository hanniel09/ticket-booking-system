package com.hanniel.ticketBookingSystem.dtos.billingAddress;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BillingAddressRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Tax ID (CPF/CNPJ) is required")
        String taxId,

        @NotBlank(message = "Postal code is required")
        String postalCode,

        @NotBlank(message = "Street is required")
        String street,

        @NotBlank(message = "Number is required")
        String number,

        String complement,

        @NotBlank(message = "Neighborhood is required")
        String neighborhood,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "State (UF) is required")
        String uf,

        @NotBlank(message = "Phone number is required")
        String phone,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = "Shipping flag is required")
        Boolean shipping
) {}
