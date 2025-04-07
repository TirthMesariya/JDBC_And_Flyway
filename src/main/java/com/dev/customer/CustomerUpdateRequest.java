package com.dev.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer phone,
        Integer age
) {
}
