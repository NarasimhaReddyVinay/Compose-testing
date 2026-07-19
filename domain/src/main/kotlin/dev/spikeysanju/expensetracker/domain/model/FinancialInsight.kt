package dev.spikeysanju.expensetracker.domain.model

data class FinancialInsight(
    val totalBudget: Double,
    val totalSpent: Double,
    val remainingAmount: Double,
    val spentPercentage: Float,
    val status: BudgetStatus
)

enum class BudgetStatus {
    SAFE, WARNING, EXCEEDED
}
