package ru.jetlabs.ts.clientwebbff.client.authprovider;

public record TokenRequest(
        String token,
        boolean needEmailConfirm
) {
}
