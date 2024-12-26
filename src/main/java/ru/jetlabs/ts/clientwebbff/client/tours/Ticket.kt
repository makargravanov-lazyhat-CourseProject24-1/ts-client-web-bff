package ru.jetlabs.ts.clientwebbff.client.tours

import java.time.LocalDate
import java.time.LocalDateTime

data class Ticket(
    val id: Long,
    val tourId: Long,
    val userId: Long,
    val agencyId: Long,
    val tourCost: Double,
    val transportCost: Double?,
    val createdAt: LocalDateTime,
    val additionalUsers: List<Long>,
    val endDate: LocalDate,
    val status: TicketStatus,
    val startDate: LocalDate
)

enum class TicketStatus {
    CREATED, PENDING, PAYED, CANCELLED
}