package ru.jetlabs.ts.clientwebbff.client.userservice;

public record UserUpdatePasswordForm(
        String previousPassword,
        String newPassword
) {}
