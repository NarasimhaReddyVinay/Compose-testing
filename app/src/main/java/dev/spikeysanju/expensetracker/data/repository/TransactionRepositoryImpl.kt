package dev.spikeysanju.expensetracker.data.repository

import dev.spikeysanju.expensetracker.data.local.TransactionDao
import dev.spikeysanju.expensetracker.domain.repository.TransactionRepository
import dev.spikeysanju.expensetracker.model.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    override fun getAllSingleTransaction(transactionType: String): Flow<List<Transaction>> =
        if (transactionType == "Overall") {
            getAllTransactions()
        } else {
            transactionDao.getAllSingleTransaction(transactionType)
        }

    override fun getByID(id: Int): Flow<Transaction> = transactionDao.getTransactionByID(id)

    override suspend fun insert(transaction: Transaction) = transactionDao.insertTransaction(transaction)

    override suspend fun update(transaction: Transaction) = transactionDao.updateTransaction(transaction)

    override suspend fun delete(transaction: Transaction) = transactionDao.deleteTransaction(transaction)

    override suspend fun deleteByID(id: Int) = transactionDao.deleteTransactionByID(id)

    override fun searchTransactions(query: String): Flow<List<Transaction>> =
        transactionDao.searchTransactions(query)

    override fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByDateRange(startDate, endDate)
}
