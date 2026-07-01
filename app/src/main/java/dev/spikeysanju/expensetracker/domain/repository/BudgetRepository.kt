package dev.spikeysanju.expensetracker.domain.repository

import dev.spikeysanju.expensetracker.model.Budget
import kotlinx.coroutines.flow.Flow

/**
 * BudgetRepository interface defines the contract for budget-related data operations.
 * This adheres to the Dependency Inversion Principle as the domain layer doesn't 
 * depend on the data layer implementation.
 */
interface BudgetRepository {
    fun getBudget(): Flow<Budget?>
    suspend fun setBudget(budget: Budget)
}
