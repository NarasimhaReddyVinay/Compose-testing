package dev.spikeysanju.expensetracker.data.repository

import dev.spikeysanju.expensetracker.data.local.BudgetDao
import dev.spikeysanju.expensetracker.data.local.entity.toDomain
import dev.spikeysanju.expensetracker.data.local.entity.toEntity
import dev.spikeysanju.expensetracker.domain.model.Budget
import dev.spikeysanju.expensetracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {
    override fun getBudget(): Flow<Budget?> = budgetDao.getBudget().map { it?.toDomain() }

    override suspend fun setBudget(budget: Budget) = budgetDao.setBudget(budget.toEntity())
}
