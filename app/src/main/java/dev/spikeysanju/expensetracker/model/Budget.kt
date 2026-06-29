package dev.spikeysanju.expensetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class Budget(
    @PrimaryKey val id: Int = 0, // We only need one budget for now
    val amount: Double
)
