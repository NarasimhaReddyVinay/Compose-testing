package dev.spikeysanju.expensetracker.data.repository

import dev.spikeysanju.expensetracker.data.local.BudgetDao
import dev.spikeysanju.expensetracker.domain.repository.BudgetRepository
import dev.spikeysanju.expensetracker.model.Budget
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {
    override fun getBudget(): Flow<Budget?> = budgetDao.getBudget()

    override suspend fun setBudget(budget: Budget) = budgetDao.setBudget(budget)
}
