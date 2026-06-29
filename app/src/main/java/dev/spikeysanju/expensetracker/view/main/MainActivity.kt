package dev.spikeysanju.expensetracker.view.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.spikeysanju.expensetracker.ui.screens.*
import dev.spikeysanju.expensetracker.ui.theme.ExpenseTrackerTheme
import dev.spikeysanju.expensetracker.utils.BiometricUtils
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: TransactionViewModel = viewModel()
            val isDarkMode by viewModel.getUIMode.collectAsState(initial = false)
            var isAuthenticated by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                if (BiometricUtils.isBiometricAvailable(this@MainActivity)) {
                    BiometricUtils.showBiometricPrompt(
                        activity = this@MainActivity,
                        onSuccess = { isAuthenticated = true },
                        onError = { error ->
                            Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                            // For demo purposes, we'll let it pass if error occurs (e.g. no biometric set)
                            // In a real app, you might want to force a PIN or close the app
                            isAuthenticated = true 
                        }
                    )
                } else {
                    isAuthenticated = true
                }
            }

            if (isAuthenticated) {
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
}
