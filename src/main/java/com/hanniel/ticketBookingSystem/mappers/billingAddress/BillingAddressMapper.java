package com.hanniel.ticketBookingSystem.mappers.billingAddress;

import com.hanniel.ticketBookingSystem.domain.billingAddress.BillingAddress;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressRequestDTO;
import com.hanniel.ticketBookingSystem.dtos.billingAddress.BillingAddressResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BillingAddressMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "taxId", source = "taxId", qualifiedByName = "maskTaxId")
    @Mapping(target = "phone", source = "phone", qualifiedByName = "maskPhone")
    @Mapping(target = "email", source = "email", qualifiedByName = "maskEmail")
    BillingAddressResponseDTO toResponse(BillingAddress billingAddress);

    @Mapping(target = "user", ignore = true)
    BillingAddress toEntity(BillingAddressRequestDTO request);

    @Mapping(target = "user", ignore = true)
    void updateEntityFromRequest(BillingAddressRequestDTO request, @MappingTarget BillingAddress billingAddress);

    @Named("maskTaxId")
    default String maskTaxId(String taxId) {
        if (taxId == null) return null;
        String clean = taxId.replaceAll("[^0-9a-zA-Z]", "");
        if (clean.length() <= 4) return "****";
        return clean.substring(0, 3) + ".".concat("*".repeat(Math.max(1, clean.length() - 5))).concat("-").concat(clean.substring(clean.length() - 2));
    }

    @Named("maskPhone")
    default String maskPhone(String phone) {
        if (phone == null) return null;
        String clean = phone.replaceAll("[^0-9]", "");
        if (clean.length() <= 4) return "****";
        return "+".concat(clean.substring(0, 2)).concat("*****").concat(clean.substring(clean.length() - 2));
    }

    @Named("maskEmail")
    default String maskEmail(String email) {
        if (email == null) return null;
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) return "f***@mail.com";
        String local = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);
        if (local.length() <= 2) {
            return local.charAt(0) + "*@" + domain;
        }
        String maskedLocal = local.charAt(0) + "*".repeat(local.length() - 2) + local.charAt(local.length() - 1);
        return maskedLocal + "@" + domain;
    }
}
