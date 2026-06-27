package com.hanniel.ticketBookingSystem.domain.billingAddress;

import com.hanniel.ticketBookingSystem.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "billing_address")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "postal_code")
    private String postalCode;

    private String street;

    private String number;

    private String complement;

    private String neighborhood;

    private String city;

    private String uf;

    private String phone;

    private String email;

    @Column(name = "is_shipping")
    private boolean shipping;
}
