package dev.spikeysanju.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.spikeysanju.expensetracker.domain.model.Budget

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey val id: Int = 0,
    val amount: Double
)

fun BudgetEntity.toDomain() = Budget(id, amount)
fun Budget.toEntity() = BudgetEntity(id, amount)
