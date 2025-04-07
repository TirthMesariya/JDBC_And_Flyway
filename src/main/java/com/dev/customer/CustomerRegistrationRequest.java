package com.dev.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        int phone,
        int age
) {
}
