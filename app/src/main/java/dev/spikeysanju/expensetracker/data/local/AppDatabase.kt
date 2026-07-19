package dev.spikeysanju.expensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.spikeysanju.expensetracker.data.local.entity.BudgetEntity
import dev.spikeysanju.expensetracker.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class, BudgetEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTransactionDao(): TransactionDao
    abstract fun getBudgetDao(): BudgetDao
}
