package ru.jetlabs.ts.clientwebbff.client.agency

import java.time.LocalDateTime

data class Agency(
    val id: Long,
    val ownerId: Long,
    val createdAt: LocalDateTime,
)