package dev.spikeysanju.expensetracker.view.main.viewmodel

import android.util.Log
import app.cash.turbine.test
import dev.spikeysanju.expensetracker.data.local.datastore.UIModeImpl
import dev.spikeysanju.expensetracker.domain.model.Transaction
import dev.spikeysanju.expensetracker.domain.repository.BudgetRepository
import dev.spikeysanju.expensetracker.domain.repository.TransactionRepository
import dev.spikeysanju.expensetracker.domain.usecase.*
import dev.spikeysanju.expensetracker.services.exportcsv.ExportCsvService
import dev.spikeysanju.expensetracker.utils.viewState.ViewState
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    
    private lateinit var viewModel: TransactionViewModel
    private val transactionRepository: TransactionRepository = mockk()
    private val budgetRepository: BudgetRepository = mockk()
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase = mockk()
    private val addTransactionUseCase: AddTransactionUseCase = mockk()
    private val searchTransactionsUseCase: SearchTransactionsUseCase = mockk()
    private val exportService: ExportCsvService = mockk()
    private val uiModeDataStore: UIModeImpl = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        
        // Mock getBudget which is called during ViewModel initialization
        every { budgetRepository.getBudget() } returns flowOf(null)
        every { uiModeDataStore.uiMode } returns flowOf(false)

        viewModel = TransactionViewModel(
            transactionRepository,
            budgetRepository,
            getAllTransactionsUseCase,
            addTransactionUseCase,
            searchTransactionsUseCase,
            exportService,
            uiModeDataStore,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllTransaction should emit Success state when transactions are available`() = runTest {
        // Given
        val transactions = listOf(
            Transaction("Lunch", 200.0, "Expense", "Food", "2023-10-01", "Lunch note", id = 1)
        )
        every { getAllTransactionsUseCase("Overall") } returns flowOf(transactions)

        // When
        viewModel.getAllTransaction("Overall")
        
        // Then
        viewModel.uiState.test {
            // Initial state is Loading
            assertEquals(ViewState.Loading, awaitItem())
            // After calling getAllTransaction, it should be Success
            val result = awaitItem()
            assert(result is ViewState.Success)
            assertEquals(transactions, (result as ViewState.Success).data)
        }
    }
}
