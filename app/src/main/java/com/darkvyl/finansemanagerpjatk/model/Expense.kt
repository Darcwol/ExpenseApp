package com.darkvyl.finansemanagerpjatk.model

import java.time.LocalDateTime

data class Expense(
        val where: String,
        val cost: Double,
        val date: LocalDateTime,
        val category: String
)