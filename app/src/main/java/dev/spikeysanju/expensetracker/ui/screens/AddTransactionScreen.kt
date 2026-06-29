package dev.spikeysanju.expensetracker.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.utils.OCRManager
import dev.spikeysanju.expensetracker.view.add.components.AddTransactionForm
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(navController: NavController, viewModel: TransactionViewModel) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            isProcessing = true
            OCRManager.extractTextFromImage(context, it,
                onSuccess = { text ->
                    isProcessing = false
                    val detectedAmount = OCRManager.findAmountInText(text)
                    if (detectedAmount != null) {
                        amount = detectedAmount.toString()
                    }
                    // Try to guess a title from the first line
                    title = text.lines().firstOrNull { it.isNotBlank() } ?: ""
                },
                onError = {
                    isProcessing = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { photoLauncher.launch("image/*") }) {
                        if (isProcessing) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Add, contentDescription = "Scan Receipt")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            AddTransactionForm(
                initialTitle = title,
                initialAmount = amount
            ) { newTitle, newAmount, type, tag, date, note ->
                val amountDouble = newAmount.toDoubleOrNull() ?: 0.0
                if (newTitle.isNotEmpty() && amountDouble > 0) {
                    val transaction = Transaction(
                        title = newTitle,
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
