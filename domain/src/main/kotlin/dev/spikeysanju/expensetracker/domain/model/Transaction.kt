package dev.spikeysanju.expensetracker.domain.model

import java.io.Serializable
import java.text.DateFormat

data class Transaction(
    val title: String,
    val amount: Double,
    val transactionType: String,
    val tag: String,
    val date: String,
    val note: String,
    val frequency: String = "Once",
    val currencyCode: String = "INR",
    val createdAt: Long = System.currentTimeMillis(),
    val id: Int = 0
) : Serializable {
    val createdAtDateFormat: String
        get() = DateFormat.getDateTimeInstance().format(createdAt)
}
