package dev.spikeysanju.expensetracker.domain.usecase

import dev.spikeysanju.expensetracker.domain.model.BudgetStatus
import dev.spikeysanju.expensetracker.domain.model.FinancialInsight
import dev.spikeysanju.expensetracker.domain.repository.BudgetRepository
import dev.spikeysanju.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * GetFinancialInsightsUseCase encapsulates the business logic for analyzing 
 * the user's financial status.
 */
class GetFinancialInsightsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository
) {
    /**
     * Executes the use case and returns a Flow of FinancialInsight.
     */
    operator fun invoke(): Flow<FinancialInsight> {
        return combine(
            transactionRepository.getAllTransactions(),
            budgetRepository.getBudget()
        ) { transactions, budget ->
            val totalBudget = budget?.amount ?: 0.0
            val totalSpent = transactions
                .filter { it.transactionType == "Expense" }
                .sumOf { it.amount }
            
            val remaining = totalBudget - totalSpent
            val percentage = if (totalBudget > 0) (totalSpent / totalBudget).toFloat() else 0f
            
            val status = when {
                percentage >= 1.0f -> BudgetStatus.EXCEEDED
                percentage >= 0.8f -> BudgetStatus.WARNING
                else -> BudgetStatus.SAFE
            }
            
            FinancialInsight(
                totalBudget = totalBudget,
                totalSpent = totalSpent,
                remainingAmount = remaining,
                spentPercentage = percentage,
                status = status
            )
        }
    }
}
