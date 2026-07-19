package dev.spikeysanju.expensetracker.domain.usecase

import dev.spikeysanju.expensetracker.domain.model.Transaction
import dev.spikeysanju.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(transactionType: String = "Overall"): Flow<List<Transaction>> {
        return repository.getAllSingleTransaction(transactionType)
    }
}
