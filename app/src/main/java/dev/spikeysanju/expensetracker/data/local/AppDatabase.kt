package dev.spikeysanju.expensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.spikeysanju.expensetracker.model.Budget
import dev.spikeysanju.expensetracker.model.Transaction

@Database(
    entities = [Transaction::class, Budget::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTransactionDao(): TransactionDao
    abstract fun getBudgetDao(): BudgetDao
}
