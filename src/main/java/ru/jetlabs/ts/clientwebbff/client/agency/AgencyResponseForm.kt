package ru.jetlabs.ts.clientwebbff.client.agency

import java.time.LocalDateTime

data class AgencyResponseForm(
    val id: Long,
    val ownerId: Long,
    val name: String,
    val createdAt: LocalDateTime,
)
