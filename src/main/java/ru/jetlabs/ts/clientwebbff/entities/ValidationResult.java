package ru.jetlabs.ts.clientwebbff.entities;

public record ValidationResult(
        boolean isValid,
        Long userId,
        String newToken
) {}