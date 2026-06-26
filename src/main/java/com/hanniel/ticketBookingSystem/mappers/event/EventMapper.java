package com.hanniel.ticketBookingSystem.mappers.event;

import com.hanniel.ticketBookingSystem.domain.event.Event;
import com.hanniel.ticketBookingSystem.dtos.event.EventRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.event.EventResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventResponseDTO toResponse(Event event);
    Event toEntity(EventRequestDTO request);
    void updateEntityFromRequest(EventRequestDTO request, @MappingTarget Event event);
}
