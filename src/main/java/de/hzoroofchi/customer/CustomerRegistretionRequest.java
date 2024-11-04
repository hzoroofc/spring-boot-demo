package de.hzoroofchi.customer;

public record CustomerRegistretionRequest(
        String name,
        String email,
        String adress,
        Integer age,
        Integer id
) {
}
