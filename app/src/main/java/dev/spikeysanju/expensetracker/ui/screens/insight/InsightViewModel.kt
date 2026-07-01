package dev.spikeysanju.expensetracker.ui.screens.insight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.spikeysanju.expensetracker.domain.model.FinancialInsight
import dev.spikeysanju.expensetracker.domain.usecase.GetFinancialInsightsUseCase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class InsightViewModel @Inject constructor(
    private val getFinancialInsightsUseCase: GetFinancialInsightsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<InsightUiState>(InsightUiState.Loading)
    val uiState: StateFlow<InsightUiState> = _uiState.asStateFlow()

    init {
        loadInsights()
    }

    private fun loadInsights() {
        getFinancialInsightsUseCase()
            .onEach { insight ->
                _uiState.value = InsightUiState.Success(insight)
            }
            .catch { error ->
                _uiState.value = InsightUiState.Error(error.message ?: "Unknown Error")
            }
            .launchIn(viewModelScope)
    }
}

sealed class InsightUiState {
    object Loading : InsightUiState()
    data class Success(val insight: FinancialInsight) : InsightUiState()
    data class Error(val message: String) : InsightUiState()
}
