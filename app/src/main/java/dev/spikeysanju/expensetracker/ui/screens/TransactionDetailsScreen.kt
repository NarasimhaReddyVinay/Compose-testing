package dev.spikeysanju.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.spikeysanju.expensetracker.utils.viewState.ViewState
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailsScreen(navController: NavController, viewModel: TransactionViewModel, transactionId: Int) {
    val detailState by viewModel.detailState.collectAsState()

    LaunchedEffect(transactionId) {
        viewModel.getByID(transactionId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.deleteByID(transactionId)
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                    IconButton(onClick = {
                        navController.navigate("edit_transaction/$transactionId")
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            when (val state = detailState) {
                is ViewState.Loading -> CircularProgressIndicator()
                is ViewState.Empty -> Text("Transaction not found")
                is ViewState.Success -> {
                    val transaction = state.data
                    Text(text = transaction.title, style = MaterialTheme.typography.headlineLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Amount: ₹${transaction.amount}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Type: ${transaction.transactionType}")
                    Text(text = "Tag: ${transaction.tag}")
                    Text(text = "Date: ${transaction.date}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Note:", style = MaterialTheme.typography.labelLarge)
                    Text(text = transaction.note)
                }
                is ViewState.Error -> Text("Error loading details")
            }
        }
    }
}
