package dev.spikeysanju.expensetracker.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.spikeysanju.expensetracker.data.repository.BudgetRepositoryImpl
import dev.spikeysanju.expensetracker.data.repository.TransactionRepositoryImpl
import dev.spikeysanju.expensetracker.domain.repository.BudgetRepository
import dev.spikeysanju.expensetracker.domain.repository.TransactionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        budgetRepositoryImpl: BudgetRepositoryImpl
    ): BudgetRepository
}
