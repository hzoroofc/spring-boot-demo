package de.hzoroofchi.customer;

public record CustomerUpdateRequest
        (String name,
        String email,
        String address,
        Integer age) {
}

