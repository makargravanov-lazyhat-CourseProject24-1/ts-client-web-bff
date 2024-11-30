package ru.jetlabs.ts.clientwebbff.client.authprovider;

public record TokenResponse(
        String token,
        Long userId,
        boolean emailConfirmed
) {
}
