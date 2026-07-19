package dev.spikeysanju.expensetracker.data.local.source

import dev.spikeysanju.expensetracker.data.local.TransactionDao
import dev.spikeysanju.expensetracker.data.local.entity.toDomain
import dev.spikeysanju.expensetracker.data.local.entity.toEntity
import dev.spikeysanju.expensetracker.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionLocalDataSourceImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionDataSource {
    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.getAllTransactions().map { entities -> entities.map { it.toDomain() } }

    override fun getAllSingleTransaction(transactionType: String): Flow<List<Transaction>> =
        if (transactionType == "Overall") {
            getAllTransactions()
        } else {
            transactionDao.getAllSingleTransaction(transactionType).map { entities ->
                entities.map { it.toDomain() }
            }
        }

    override fun getByID(id: Int): Flow<Transaction> =
        transactionDao.getTransactionByID(id).map { it.toDomain() }

    override suspend fun insert(transaction: Transaction) =
        transactionDao.insertTransaction(transaction.toEntity())

    override suspend fun update(transaction: Transaction) =
        transactionDao.updateTransaction(transaction.toEntity())

    override suspend fun delete(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction.toEntity())

    override suspend fun deleteByID(id: Int) = transactionDao.deleteTransactionByID(id)

    override fun searchTransactions(query: String): Flow<List<Transaction>> =
        transactionDao.searchTransactions(query).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
}
