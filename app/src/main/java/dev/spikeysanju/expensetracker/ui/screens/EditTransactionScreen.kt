package dev.spikeysanju.expensetracker.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.utils.viewState.ViewState
import dev.spikeysanju.expensetracker.view.add.components.AddTransactionForm
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel
import parseDouble

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(navController: NavController, viewModel: TransactionViewModel, transactionId: Int) {
    val detailState by viewModel.detailState.collectAsState()

    LaunchedEffect(transactionId) {
        viewModel.getByID(transactionId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Transaction") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            when (val state = detailState) {
                is ViewState.Loading -> CircularProgressIndicator()
                is ViewState.Success -> {
                    val initialTransaction = state.data
                    // In a real app, we'd pre-fill the form with initialTransaction
                    // For now, let's reuse AddTransactionForm with the onSave logic for update
                    AddTransactionForm { title, amount, type, tag, date, note ->
                        val amountDouble = parseDouble(amount)
                        if (title.isNotEmpty() && !amountDouble.isNaN()) {
                            val updatedTransaction = Transaction(
                                title = title,
                                amount = amountDouble,
                                transactionType = type,
                                tag = tag,
                                date = date,
                                note = note,
                                id = initialTransaction.id,
                                createdAt = initialTransaction.createdAt
                            )
                            viewModel.updateTransaction(updatedTransaction)
                            navController.popBackStack()
                        }
                    }
                }
                else -> Text("Error loading transaction for edit")
            }
        }
    }
}
