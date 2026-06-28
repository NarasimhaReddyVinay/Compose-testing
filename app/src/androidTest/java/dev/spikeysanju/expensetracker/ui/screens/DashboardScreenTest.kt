package dev.spikeysanju.expensetracker.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.utils.viewState.ViewState
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class DashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dashboardScreen_DisplaysTransactions() {
        val mockViewModel = mockk<TransactionViewModel>(relaxed = true)
        val transactions = listOf(
            Transaction("Coffee", 150.0, "Expense", "Food", "10/10/2023", "Nice coffee", id = 1),
            Transaction("Salary", 50000.0, "Income", "Salary", "01/10/2023", "Monthly pay", id = 2)
        )
        
        every { mockViewModel.uiState } returns MutableStateFlow(ViewState.Success(transactions))
        every { mockViewModel.transactionFilter } returns MutableStateFlow("Overall")

        composeTestRule.setContent {
            val navController = rememberNavController()
            DashboardScreen(navController = navController, viewModel = mockViewModel)
        }

        // Check if transactions are displayed
        composeTestRule.onNodeWithText("Coffee").assertIsDisplayed()
        composeTestRule.onNodeWithText("Salary").assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_SwipeToDelete_TriggersDelete() {
        val mockViewModel = mockk<TransactionViewModel>(relaxed = true)
        val transaction = Transaction("Coffee", 150.0, "Expense", "Food", "10/10/2023", "Nice coffee", id = 1)
        val transactions = listOf(transaction)
        
        every { mockViewModel.uiState } returns MutableStateFlow(ViewState.Success(transactions))
        every { mockViewModel.transactionFilter } returns MutableStateFlow("Overall")

        composeTestRule.setContent {
            val navController = rememberNavController()
            DashboardScreen(navController = navController, viewModel = mockViewModel)
        }

        // Swipe the item to the left (EndToStart) to trigger delete
        composeTestRule.onNodeWithText("Coffee").performTouchInput {
            swipeLeft()
        }

        // Verify that deleteTransaction was called in the ViewModel
        verify { mockViewModel.deleteTransaction(any()) }
    }

    @Test
    fun dashboardScreen_DisplaysEmptyState() {
        val mockViewModel = mockk<TransactionViewModel>(relaxed = true)
        
        every { mockViewModel.uiState } returns MutableStateFlow(ViewState.Empty)
        every { mockViewModel.transactionFilter } returns MutableStateFlow("Overall")

        composeTestRule.setContent {
            val navController = rememberNavController()
            DashboardScreen(navController = navController, viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("No transactions yet").assertIsDisplayed()
    }
}
