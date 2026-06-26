package com.hanniel.ticketBookingSystem.domain.order.enums;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    VERIFYING_PAYMENT,
    PAYMENT_APPROVED,
    PAYMENT_REJECTED,
    TIMEOUT,
    SOLD_OUT,
    REFUND,
    CANCELLED,
}
