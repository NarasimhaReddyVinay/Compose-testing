package dev.spikeysanju.expensetracker.view.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.spikeysanju.expensetracker.ui.screens.AddTransactionScreen
import dev.spikeysanju.expensetracker.ui.screens.DashboardScreen
import dev.spikeysanju.expensetracker.ui.screens.EditTransactionScreen
import dev.spikeysanju.expensetracker.ui.screens.TransactionDetailsScreen
import dev.spikeysanju.expensetracker.ui.screens.AboutScreen
import dev.spikeysanju.expensetracker.ui.theme.ExpenseTrackerTheme
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: TransactionViewModel = viewModel()
            val isDarkMode by viewModel.getUIMode.collectAsState(initial = false)

            ExpenseTrackerTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "dashboard") {
                    composable("dashboard") {
                        DashboardScreen(navController, viewModel)
                    }
                    composable("add_transaction") {
                        AddTransactionScreen(navController, viewModel)
                    }
                    composable(
                        "details/{transactionId}",
                        arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("transactionId") ?: return@composable
                        TransactionDetailsScreen(navController, viewModel, id)
                    }
                    composable(
                        "edit_transaction/{transactionId}",
                        arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("transactionId") ?: return@composable
                        EditTransactionScreen(navController, viewModel, id)
                    }
                    composable("about") {
                        AboutScreen(navController)
                    }
                }
            }
        }
    }
}
