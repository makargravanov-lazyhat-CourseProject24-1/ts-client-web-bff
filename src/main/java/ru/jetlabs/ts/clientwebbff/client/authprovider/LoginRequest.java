package ru.jetlabs.ts.clientwebbff.client.authprovider;

public record LoginRequest(
        String email,
        String password
) {}
