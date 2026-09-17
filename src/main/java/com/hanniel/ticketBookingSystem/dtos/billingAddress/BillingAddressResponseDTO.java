package com.hanniel.ticketBookingSystem.dtos.billingAddress;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados do endereço de cobrança retornado")
public record BillingAddressResponseDTO(
        @Schema(description = "Identificador único do endereço", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
        UUID id,

        @Schema(description = "ID do usuário proprietário do endereço", example = "b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e")
        UUID userId,

        @Schema(description = "Nome do titular", example = "João da Silva")
        String name,

        @Schema(description = "Documento fiscal (CPF/CNPJ)", example = "123.456.789-00")
        String taxId,

        @Schema(description = "Código postal (CEP)", example = "01001-000")
        String postalCode,

        @Schema(description = "Logradouro", example = "Praça da Sé")
        String street,

        @Schema(description = "Número", example = "100")
        String number,

        @Schema(description = "Complemento", example = "Apto 101")
        String complement,

        @Schema(description = "Bairro", example = "Sé")
        String neighborhood,

        @Schema(description = "Cidade", example = "São Paulo")
        String city,

        @Schema(description = "Unidade Federativa (UF)", example = "SP")
        String uf,

        @Schema(description = "Telefone para contato", example = "(11) 98765-4321")
        String phone,

        @Schema(description = "Email de faturamento", example = "faturamento@example.com")
        String email,

        @Schema(description = "Indica se também é endereço de entrega", example = "true")
        Boolean shipping
) {}
