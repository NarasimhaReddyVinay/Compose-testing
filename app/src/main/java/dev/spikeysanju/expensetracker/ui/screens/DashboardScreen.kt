package dev.spikeysanju.expensetracker.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.utils.viewState.ViewState
import dev.spikeysanju.expensetracker.view.dashboard.components.AnalyticsChart
import dev.spikeysanju.expensetracker.view.dashboard.components.ChartData
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: TransactionViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val filter by viewModel.transactionFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    LaunchedEffect(filter) {
        viewModel.getAllTransaction(filter)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Tracker") },
                actions = {
                    IconButton(onClick = { navController.navigate("about") }) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_transaction") }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchTransactions(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search transactions...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchTransactions("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )

            when (val state = uiState) {
                is ViewState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                is ViewState.Empty -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No transactions yet") }
                is ViewState.Success -> {
                    val transactions = state.data
                    val (income, expense) = transactions.partition { it.transactionType == "Income" }
                    val totalIncome = income.sumOf { it.amount }.toFloat()
                    val totalExpense = expense.sumOf { it.amount }.toFloat()

                    AnalyticsChart(
                        data = listOf(
                            ChartData(Color(0xFF4CAF50), totalIncome, "Income"),
                            ChartData(Color(0xFFF44336), totalExpense, "Expense")
                        ),
                        modifier = Modifier.height(300.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(
                            items = transactions,
                            key = { it.id }
                        ) { transaction ->
                            SwipeToActionItem(
                                transaction = transaction,
                                onDismissedToStart = {
                                    viewModel.deleteTransaction(transaction)
                                },
                                onDismissedToEnd = {
                                    navController.navigate("edit_transaction/${transaction.id}")
                                },
                                onClick = {
                                    navController.navigate("details/${transaction.id}")
                                }
                            )
                        }
                    }
                }
                is ViewState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Error loading transactions") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToActionItem(
    transaction: Transaction,
    onDismissedToStart: () -> Unit,
    onDismissedToEnd: () -> Unit,
    onClick: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDismissedToStart()
                    true
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onDismissedToEnd()
                    false // Don't dismiss, we are navigating
                }
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> Color.LightGray
                    SwipeToDismissBoxValue.StartToEnd -> Color(0xFF2196F3) // Edit Blue
                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFF44336) // Delete Red
                }, label = "background"
            )

            val alignment = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                else -> Alignment.Center
            }

            val icon = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Edit
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                else -> null
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                if (icon != null) {
                    Icon(
                        icon,
                        contentDescription = if (direction == SwipeToDismissBoxValue.StartToEnd) "Edit" else "Delete",
                        tint = Color.White
                    )
                }
            }
        }
    ) {
        TransactionItem(transaction, onClick)
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = transaction.title, fontWeight = FontWeight.Bold)
                Text(text = transaction.tag, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "₹${transaction.amount}",
                color = if (transaction.transactionType == "Income") Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
    }
}
