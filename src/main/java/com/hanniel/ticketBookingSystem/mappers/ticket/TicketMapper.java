package com.hanniel.ticketBookingSystem.mappers.ticket;

import com.hanniel.ticketBookingSystem.domain.ticket.Ticket;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.ticket.TicketResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "ticketTypeId", source = "ticketType.id")
    @Mapping(target = "orderId", source = "order.id")
    TicketResponseDTO toResponse(Ticket ticket);

    @Mapping(target = "ticketType", ignore = true)
    @Mapping(target = "order", ignore = true)
    Ticket toEntity(TicketRequestDTO request);

    @Mapping(target = "ticketType", ignore = true)
    @Mapping(target = "order", ignore = true)
    void updateEntityFromRequest(TicketRequestDTO request, @MappingTarget Ticket ticket);
}
