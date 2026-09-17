package com.hanniel.ticketBookingSystem.dtos.billingAddress;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Requisição para criação ou atualização de endereço de cobrança")
public record BillingAddressRequestDTO(
        @Schema(description = "Nome do titular", example = "João da Silva")
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Documento fiscal (CPF/CNPJ)", example = "123.456.789-00")
        @NotBlank(message = "Tax ID (CPF/CNPJ) is required")
        String taxId,

        @Schema(description = "Código de Endereçamento Postal (CEP)", example = "01001-000")
        @NotBlank(message = "Postal code is required")
        String postalCode,

        @Schema(description = "Logradouro", example = "Praça da Sé")
        @NotBlank(message = "Street is required")
        String street,

        @Schema(description = "Número", example = "100")
        @NotBlank(message = "Number is required")
        String number,

        @Schema(description = "Complemento", example = "Apto 101")
        String complement,

        @Schema(description = "Bairro", example = "Sé")
        @NotBlank(message = "Neighborhood is required")
        String neighborhood,

        @Schema(description = "Cidade", example = "São Paulo")
        @NotBlank(message = "City is required")
        String city,

        @Schema(description = "Unidade Federativa (UF)", example = "SP")
        @NotBlank(message = "State (UF) is required")
        String uf,

        @Schema(description = "Telefone para contato", example = "(11) 98765-4321")
        @NotBlank(message = "Phone number is required")
        String phone,

        @Schema(description = "Email de faturamento", example = "faturamento@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "Indica se também é endereço de entrega", example = "true")
        @NotNull(message = "Shipping flag is required")
        Boolean shipping
) {}
