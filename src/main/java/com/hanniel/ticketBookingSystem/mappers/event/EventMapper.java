package com.hanniel.ticketBookingSystem.mappers.event;

import com.hanniel.ticketBookingSystem.domain.event.Event;
import com.hanniel.ticketBookingSystem.dtos.event.EventRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.event.EventResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "eventDate", expression = "java(com.hanniel.ticketBookingSystem.helper.date.DateHelper.toBrDateString(event.getEventDate()))")
    EventResponseDTO toResponse(Event event);

    @Mapping(target = "eventDate", expression = "java(com.hanniel.ticketBookingSystem.helper.date.DateHelper.toOffsetDateTime(request.eventDate()))")
    Event toEntity(EventRequestDTO request);

    @Mapping(target = "eventDate", expression = "java(com.hanniel.ticketBookingSystem.helper.date.DateHelper.toOffsetDateTime(request.eventDate()))")
    void updateEntityFromRequest(EventRequestDTO request, @MappingTarget Event event);
}
