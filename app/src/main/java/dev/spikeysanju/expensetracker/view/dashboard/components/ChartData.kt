package dev.spikeysanju.expensetracker.view.dashboard.components

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ChartData(val color: Color, val value: Float, val label: String)
