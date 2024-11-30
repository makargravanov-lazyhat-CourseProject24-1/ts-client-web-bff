package ru.jetlabs.ts.clientwebbff.client.userservice;

public record UserCreateForm(
        String firstName,
        String lastName,
        String email,
        String password
) {}
