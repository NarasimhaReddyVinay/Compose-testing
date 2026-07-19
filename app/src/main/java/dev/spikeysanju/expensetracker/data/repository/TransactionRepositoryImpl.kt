package dev.spikeysanju.expensetracker.data.repository

import dev.spikeysanju.expensetracker.data.local.source.TransactionDataSource
import dev.spikeysanju.expensetracker.di.LocalDataSource
import dev.spikeysanju.expensetracker.domain.model.Transaction
import dev.spikeysanju.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    @LocalDataSource private val localDataSource: TransactionDataSource
) : TransactionRepository {
    override fun getAllTransactions(): Flow<List<Transaction>> =
        localDataSource.getAllTransactions()

    override fun getAllSingleTransaction(transactionType: String): Flow<List<Transaction>> =
        localDataSource.getAllSingleTransaction(transactionType)

    override fun getByID(id: Int): Flow<Transaction> =
        localDataSource.getByID(id)

    override suspend fun insert(transaction: Transaction) =
        localDataSource.insert(transaction)

    override suspend fun update(transaction: Transaction) =
        localDataSource.update(transaction)

    override suspend fun delete(transaction: Transaction) =
        localDataSource.delete(transaction)

    override suspend fun deleteByID(id: Int) = localDataSource.deleteByID(id)

    override fun searchTransactions(query: String): Flow<List<Transaction>> =
        localDataSource.searchTransactions(query)

    override fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> =
        localDataSource.getTransactionsByDateRange(startDate, endDate)
}
