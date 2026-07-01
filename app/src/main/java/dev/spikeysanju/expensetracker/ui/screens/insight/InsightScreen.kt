package dev.spikeysanju.expensetracker.ui.screens.insight

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.spikeysanju.expensetracker.domain.model.BudgetStatus
import dev.spikeysanju.expensetracker.domain.model.FinancialInsight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightScreen(navController: NavController, viewModel: InsightViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financial Insights") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is InsightUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is InsightUiState.Error -> Text(text = state.message, color = Color.Red, modifier = Modifier.align(Alignment.Center))
                is InsightUiState.Success -> InsightContent(state.insight)
            }
        }
    }
}

@Composable
fun InsightContent(insight: FinancialInsight) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when (insight.status) {
                    BudgetStatus.SAFE -> Color(0xFFE8F5E9)
                    BudgetStatus.WARNING -> Color(0xFFFFF3E0)
                    BudgetStatus.EXCEEDED -> Color(0xFFFFEBEE)
                }
            )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = when (insight.status) {
                        BudgetStatus.SAFE -> "You're doing great!"
                        BudgetStatus.WARNING -> "Approaching budget limit"
                        BudgetStatus.EXCEEDED -> "Budget exceeded!"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = when (insight.status) {
                        BudgetStatus.SAFE -> Color(0xFF2E7D32)
                        BudgetStatus.WARNING -> Color(0xFFEF6C00)
                        BudgetStatus.EXCEEDED -> Color(0xFFC62828)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { insight.spentPercentage.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(12.dp),
                    color = when (insight.status) {
                        BudgetStatus.SAFE -> Color(0xFF4CAF50)
                        BudgetStatus.WARNING -> Color(0xFFFF9800)
                        BudgetStatus.EXCEEDED -> Color(0xFFF44336)
                    },
                    trackColor = Color.LightGray
                )
            }
        }

        InsightRow("Monthly Budget", "₹${insight.totalBudget}")
        InsightRow("Total Spent", "₹${insight.totalSpent}")
        InsightRow("Remaining", "₹${insight.remainingAmount}")
    }
}

@Composable
fun InsightRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}
