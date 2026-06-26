package com.hanniel.ticketBookingSystem.exceptions.global;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
