package dev.spikeysanju.expensetracker.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.spikeysanju.expensetracker.domain.model.Transaction

@Entity(tableName = "all_transactions")
data class TransactionEntity(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "amount") var amount: Double,
    @ColumnInfo(name = "transactionType") var transactionType: String,
    @ColumnInfo(name = "tag") var tag: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "note") var note: String,
    @ColumnInfo(name = "frequency") var frequency: String = "Once",
    @ColumnInfo(name = "currencyCode") var currencyCode: String = "INR",
    @ColumnInfo(name = "createdAt") var createdAt: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
)

fun TransactionEntity.toDomain() = Transaction(
    title = title,
    amount = amount,
    transactionType = transactionType,
    tag = tag,
    date = date,
    note = note,
    frequency = frequency,
    currencyCode = currencyCode,
    createdAt = createdAt,
    id = id
)

fun Transaction.toEntity() = TransactionEntity(
    title = title,
    amount = amount,
    transactionType = transactionType,
    tag = tag,
    date = date,
    note = note,
    frequency = frequency,
    currencyCode = currencyCode,
    createdAt = createdAt,
    id = id
)
