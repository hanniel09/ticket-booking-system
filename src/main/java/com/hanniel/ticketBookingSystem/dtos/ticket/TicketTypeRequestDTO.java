package com.hanniel.ticketBookingSystem.dtos.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Requisição para criação ou atualização de tipo de ingresso")
public record TicketTypeRequestDTO(
        @Schema(description = "Identificador do evento ao qual o tipo de ingresso pertence", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
        @NotNull(message = "O ID do evento é obrigatório")
        UUID eventId,

        @Schema(description = "Nome ou categoria do ingresso (ex: Pista, VIP, Camarote)", example = "VIP Open Bar")
        @NotBlank(message = "O nome da categoria do ingresso é obrigatório")
        String name,

        @Schema(description = "Preço unitário do ingresso", example = "250.00")
        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.0", inclusive = true, message = "O preço não pode ser negativo")
        BigDecimal price,

        @Schema(description = "Quantidade disponível no lote/estoque", example = "500")
        @NotNull(message = "A quantidade inicial é obrigatória")
        @Min(value = 1, message = "A quantidade inicial deve ser de no mínimo 1 ingresso")
        Long quantityAvailable
) {}
