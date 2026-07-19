package dev.spikeysanju.expensetracker.data.local

import androidx.room.*
import dev.spikeysanju.expensetracker.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    // used to insert new transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    // used to update existing transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTransaction(transaction: TransactionEntity)

    // used to delete transaction
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    // get all saved transaction list
    @Query("SELECT * FROM all_transactions ORDER by createdAt DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    // get all income or expense list by transaction type param
    @Query("SELECT * FROM all_transactions WHERE transactionType == :transactionType ORDER by createdAt DESC")
    fun getAllSingleTransaction(transactionType: String): Flow<List<TransactionEntity>>

    // get single transaction by id
    @Query("SELECT * FROM all_transactions WHERE id = :id")
    fun getTransactionByID(id: Int): Flow<TransactionEntity>

    // delete transaction by id
    @Query("DELETE FROM all_transactions WHERE id = :id")
    suspend fun deleteTransactionByID(id: Int)

    // Search transactions by title
    @Query("SELECT * FROM all_transactions WHERE title LIKE '%' || :query || '%' ORDER by createdAt DESC")
    fun searchTransactions(query: String): Flow<List<TransactionEntity>>

    // Filter by date range
    @Query("SELECT * FROM all_transactions WHERE createdAt BETWEEN :startDate AND :endDate ORDER by createdAt DESC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<TransactionEntity>>
}
