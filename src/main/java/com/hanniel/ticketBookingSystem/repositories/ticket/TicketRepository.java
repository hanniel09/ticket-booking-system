package com.hanniel.ticketBookingSystem.repositories.ticket;

import com.hanniel.ticketBookingSystem.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
