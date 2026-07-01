package dev.spikeysanju.expensetracker.domain.repository

import dev.spikeysanju.expensetracker.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getAllSingleTransaction(transactionType: String): Flow<List<Transaction>>
    fun getByID(id: Int): Flow<Transaction>
    suspend fun insert(transaction: Transaction)
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)
    suspend fun deleteByID(id: Int)
    fun searchTransactions(query: String): Flow<List<Transaction>>
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>
}
