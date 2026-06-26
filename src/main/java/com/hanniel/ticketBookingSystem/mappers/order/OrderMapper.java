package com.hanniel.ticketBookingSystem.mappers.order;

import com.hanniel.ticketBookingSystem.domain.order.Order;
import com.hanniel.ticketBookingSystem.dtos.order.OrderRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.order.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "ticketTypeId", source = "ticketType.id")
    @Mapping(target = "billingAddressId", source = "billingAddress.id")
    OrderResponseDTO toResponse(Order order);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ticketType", ignore = true)
    @Mapping(target = "billingAddress", ignore = true)
    Order toEntity(OrderRequestDTO request);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ticketType", ignore = true)
    @Mapping(target = "billingAddress", ignore = true)
    void updateEntityFromRequest(OrderRequestDTO request, @MappingTarget Order order);
}
