package dev.spikeysanju.expensetracker.domain.usecase

import dev.spikeysanju.expensetracker.domain.model.Transaction
import dev.spikeysanju.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(query: String): Flow<List<Transaction>> {
        return repository.searchTransactions(query)
    }
}
