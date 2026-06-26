package com.hanniel.ticketBookingSystem.mappers.ticket;

import com.hanniel.ticketBookingSystem.domain.ticket.TicketType;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketTypeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TicketTypeMapper {
    TicketTypeResponseDTO toResponse(TicketType ticketType);
    TicketType toEntity(TicketTypeRequestDTO request);
    void updateEntityFromRequest(TicketTypeRequestDTO request, @MappingTarget TicketType ticketType);
}
