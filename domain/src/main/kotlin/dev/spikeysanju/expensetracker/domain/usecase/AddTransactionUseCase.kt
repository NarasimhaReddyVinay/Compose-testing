package dev.spikeysanju.expensetracker.domain.usecase

import dev.spikeysanju.expensetracker.domain.model.Transaction
import dev.spikeysanju.expensetracker.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.insert(transaction)
    }
}
