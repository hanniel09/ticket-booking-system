package com.hanniel.ticketBookingSystem.repositories.billingAddress;

import com.hanniel.ticketBookingSystem.domain.billingAddress.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillingAddressRepository extends JpaRepository<BillingAddress, UUID> {
    List<BillingAddress> findByUserId(UUID userId);
}
