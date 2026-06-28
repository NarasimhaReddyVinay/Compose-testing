package dev.spikeysanju.expensetracker.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.view.add.components.AddTransactionForm
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(navController: NavController, viewModel: TransactionViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            AddTransactionForm { title, amount, type, tag, date, note ->
                val amountDouble = amount.toDoubleOrNull() ?: 0.0
                if (title.isNotEmpty() && amountDouble > 0) {
                    val transaction = Transaction(
                        title = title,
                        amount = amountDouble,
                        transactionType = type,
                        tag = tag,
                        date = date,
                        note = note
                    )
                    viewModel.insertTransaction(transaction)
                    navController.popBackStack()
                }
            }
        }
    }
}
