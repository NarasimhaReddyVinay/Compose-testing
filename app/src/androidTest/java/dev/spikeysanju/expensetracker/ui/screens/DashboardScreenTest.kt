package dev.spikeysanju.expensetracker.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.utils.viewState.ViewState
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel
import io.mockk.every
import io.mockk.mockk
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
            Transaction("Coffee", 150.0, "Expense", "Food", "10/10/2023", "Nice coffee"),
            Transaction("Salary", 50000.0, "Income", "Salary", "01/10/2023", "Monthly pay")
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
        composeTestRule.onNodeWithText("₹150.0").assertIsDisplayed()
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
